package com.app.services;

import java.io.IOException;
import java.util.List;
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

import com.app.entites.CategoryCoupon;
import com.app.entites.Coupon;
import com.app.entites.Order;
import com.app.entites.Product;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CouponDTO;
import com.app.payloads.CouponResponse;
import com.app.payloads.ProductDTO;
import com.app.repositories.CartRepo;
import com.app.repositories.CategoryCouponRepo;
import com.app.repositories.CouponRepo;
import com.app.repositories.ProductRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class CouponServiceImpl implements CouponService {

	@Autowired
	private CouponRepo couponRepo;

	@Autowired
	private CategoryCouponRepo categoryCouponRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private FileService fileService;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${project.image}")
	private String path;

	@Override
	public CouponDTO addCoupon(Long categoryId, Coupon coupon) {

		CategoryCoupon categoryCoupon = categoryCouponRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

		boolean isCouponNotPresent = true;

		List<Coupon> coupons = categoryCoupon.getCoupons();

		for (int i = 0; i < coupons.size(); i++) {
			if (coupons.get(i).getCode().equals(coupon.getCode())) {
				isCouponNotPresent = false;
				break;
			}
		}

		if (isCouponNotPresent) {
			coupon.setConditions("default condition");
			coupon.setStatus("active");
			coupon.setImage("default.png");
			coupon.setCategoryCoupon(categoryCoupon);
			Coupon savedCoupon = couponRepo.save(coupon);
			return modelMapper.map(savedCoupon, CouponDTO.class);
		} else {
			throw new APIException("Coupon already exists.");
		}

	}

	@Override
	public CouponResponse getAllCoupons(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Coupon> pageCoupons = couponRepo.findAll(pageDetails);
		List<Coupon> coupons = pageCoupons.getContent();
		List<CouponDTO> couponDTOs = coupons.stream().map(coupon -> modelMapper.map(coupon, CouponDTO.class))
				.collect(Collectors.toList());
		CouponResponse couponResponse = new CouponResponse();
		couponResponse.setContent(couponDTOs);
		couponResponse.setPageNumber(pageCoupons.getNumber());
		couponResponse.setPageSize(pageCoupons.getSize());
		couponResponse.setTotalElements(pageCoupons.getTotalElements());
		couponResponse.setTotalPages(pageCoupons.getTotalPages());
		couponResponse.setLastPage(pageCoupons.isLast());
		return couponResponse;
	}

	@Override
	public CouponDTO updateCoupon(Long couponId, Coupon coupon) {
		Coupon couponFromDB = couponRepo.findById(couponId)
				.orElseThrow(() -> new ResourceNotFoundException("Coupon", "couponId", couponId));
		if (couponFromDB == null) {
			throw new APIException("Coupon not found with couponId: " + couponId);
		}
		coupon.setImage(couponFromDB.getImage());
		coupon.setCouponId(couponFromDB.getCouponId());
		coupon.setCategoryCoupon(couponFromDB.getCategoryCoupon());
		coupon.setCode(couponFromDB.getCode());
		coupon.setStartDate(couponFromDB.getStartDate());

		Coupon savedCoupon = couponRepo.save(coupon);

		List<Product> products = productRepo.findProductsByCouponId(couponId);

		List<ProductDTO> productDTOs = products.stream().map(product -> {
			ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
			return productDTO;
		}).collect(Collectors.toList());
		return null;
	}

	@Override
	public String deleteCoupon(Long couponId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CouponDTO applyCoupon(String code, Long orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double calculateDiscount(Coupon coupon, Order order) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CouponDTO updateCouponImage(Long couponId, MultipartFile image) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
