package com.app.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.entites.Product;
import com.app.entites.ProductVariant;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.ProductVariantDTO;
import com.app.payloads.ProductVariantResponse;
import com.app.repositories.ProductRepo;
import com.app.repositories.ProductVariantRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ProductVariantServiceImpl implements ProductVariantService {

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private ProductVariantRepo variantRepo;

	@Autowired
	private FileService fileService;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${project.image}")
	private String path;

	@Override
	public ProductVariantDTO addProductVariantById(Long productId, ProductVariant variant) {
		// 找出哪一個產品需要新增變體
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		// 宣告產品變體是否存在的布林值
		boolean isProductVariantPresent = true;
		// 從指定的產品當中取出所有的變體
		List<ProductVariant> variants = product.getVariants();
		// 遊歷所有的產品變體以便確保新增的變體沒有重複
		for (int i = 0; i < variants.size(); i++) {
			if (variants.get(i).getColor().equals(variant.getColor())
					&& variants.get(i).getSku().equals(variant.getSku())) {
				isProductVariantPresent = false;
				break;
			}
		}
		// 如果新增的變體沒有重複，則進行新增
		if (isProductVariantPresent) {
			// 對持久化實體(產品變體)進行關聯
			variant.setProduct(product);
			// 修改產品的總數
			Integer quantity = product.getQuantity() + variant.getInventory();
			product.setQuantity(quantity);

			Product savedProduct = productRepo.save(product);
			ProductVariant savedProductVariant = variantRepo.save(variant);

			System.out.println("savedProduct: " + savedProduct);

			return modelMapper.map(savedProductVariant, ProductVariantDTO.class);
		} else {
			throw new APIException("ProductVariant already exists");
		}
	}

	@Override
	public List<ProductVariantDTO> addProductVariantsById(Long productId, List<ProductVariant> variants) {

		// 找出哪一個產品需要新增變體
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		// 宣告產品變體是否存在的布林值
		boolean isProductVariantsPresent = false;

		// 從指定的產品當中取出所有的變體
		List<ProductVariant> productVariants = product.getVariants();

//		// 確保此產品沒有任何變體才可以設定變體集合
		if (productVariants.isEmpty()) {
			isProductVariantsPresent = true;
		}

		// 如果新增的變體沒有重複，則進行新增
		if (isProductVariantsPresent) {

			// 對持久化實體(產品變體)進行關聯
			product.setVariants(variants);

			int q = 0;

			List<ProductVariantDTO> dtos = new ArrayList<>();

			AtomicInteger atomicQ = new AtomicInteger(q);

			variants.forEach(t -> {
				atomicQ.addAndGet(t.getInventory());
				dtos.add(modelMapper.map(t, ProductVariantDTO.class));
			});

			// 修改產品的總數
			product.setQuantity(atomicQ.get());

			Product savedProduct = productRepo.save(product);
			System.out.println("savedProduct: " + savedProduct);

			List<ProductVariant> savedProductVariants = variantRepo.saveAll(variants);
			System.out.println(savedProductVariants);

			return dtos;
		} else {
			throw new APIException("ProductVariant already exists");
		}
	}

	@Override
	public ProductVariantResponse getAllProductVariantsById(Long productId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<ProductVariant> pageProductVariants = variantRepo.findAll(pageDetails);

		List<ProductVariant> variants = pageProductVariants.getContent();

		List<ProductVariantDTO> variantDTOs = variants.stream()
				.map(variant -> modelMapper.map(variant, ProductVariantDTO.class)).collect(Collectors.toList());

		ProductVariantResponse variantResponse = new ProductVariantResponse();

		variantResponse.setContent(variantDTOs);
		variantResponse.setPageNumber(pageProductVariants.getNumber());
		variantResponse.setPageSize(pageProductVariants.getSize());
		variantResponse.setTotalElements(pageProductVariants.getTotalElements());
		variantResponse.setTotalPages(pageProductVariants.getTotalPages());
		variantResponse.setLastPage(pageProductVariants.isLast());

		return variantResponse;
	}

	@Override
	public ProductVariantDTO updateProductVariant(Long productId, ProductVariant variant) {

		Product productFromDB = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		if (productFromDB == null) {
			throw new APIException("Product not found with productId: " + productId);
		}

		ProductVariant productVariantFromDB = variantRepo.findByProductIdAndProductVariantSKU(productId,
				variant.getSku());
		if (productVariantFromDB == null) {
			throw new APIException("ProductVariant not found with prodcuctId or SKU: " + productId + variant.getSku());
		}
//		variant.setColor();
		variant.setImage(productVariantFromDB.getImage());
//		variant.setInventory();
		variant.setProduct(productVariantFromDB.getProduct());
//		variant.setSize();
//		variant.setSku();
		variant.setProductVariantId(productVariantFromDB.getProductVariantId());

		ProductVariant savedProductVariant = variantRepo.save(variant);

		Integer q = productFromDB.getQuantity() + variant.getInventory();
		productFromDB.setQuantity(q);

		List<ProductVariant> list = productFromDB.getVariants();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getProductVariantId().equals(variant.getProductVariantId())) {
				list.set(i, variant);
				break;
			}
		}
		productFromDB.setVariants(list);
		productRepo.save(productFromDB);

		return modelMapper.map(savedProductVariant, ProductVariantDTO.class);
	}

	@Override
	public ProductVariantDTO updateProductImage(Long productId, String color, MultipartFile image) throws IOException {

		Product productFromDB = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		if (productFromDB == null) {
			throw new APIException("Product not found with productId: " + productId);
		}

		ProductVariant variant = variantRepo.findByProductVariantColor(color);

		String fileName = fileService.uploadImage(path, image);
		variant.setImage(fileName);

		List<ProductVariant> list = productFromDB.getVariants();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getProductVariantId().equals(variant.getProductVariantId())) {
				list.set(i, variant);
				break;
			}
		}
		productFromDB.setVariants(list);

		Product updatedProduct = productRepo.save(productFromDB);
		System.out.println(updatedProduct);

		return modelMapper.map(variant, ProductVariantDTO.class);
	}

	@Override
	public String deleteProductVariantById(Long productId, String sku) {

		Product productFromDB = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		ProductVariant variantFromDB = variantRepo.findByProductIdAndProductVariantSKU(productId, sku);

		List<ProductVariant> list = productFromDB.getVariants();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getSku().equalsIgnoreCase(sku)) {
				list.remove(i);
				break;
			}
		}
		productFromDB.setVariants(list);

		productRepo.save(productFromDB);
		
		if (variantFromDB == null) {
			return "ProductVariant with productId: " + productId + " ,with sku: " + sku + " not found.";
		} else {
			variantRepo.delete(variantFromDB);
			return "ProductVariant with productId: " + productId + " ,with sku: " + sku + " deleted successfully !!!";
		}

	}

}
