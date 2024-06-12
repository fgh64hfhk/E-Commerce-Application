package com.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.config.AppConstants;
import com.app.entites.Address;
import com.app.entites.Cart;
import com.app.entites.Role;
import com.app.entites.User;
import com.app.repositories.CartRepo;
import com.app.repositories.RoleRepo;
import com.app.repositories.UserRepo;
import com.app.services.UserCreateService;
import com.app.utilts.JsonParserUtils;
import com.app.utilts.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@SecurityScheme(name = "E-Commerce Application", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class ECommerceApplication implements CommandLineRunner {

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CartRepo cartRepo;
	
	@Autowired
	private UserCreateService createService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {

		Role adminRole = new Role();
		adminRole.setRoleId(AppConstants.ADMIN_ID);
		adminRole.setRoleName("ADMIN");

		Role userRole = new Role();
		userRole.setRoleId(AppConstants.USER_ID);
		userRole.setRoleName("USER");

		List<Role> roles = List.of(adminRole, userRole);
		// 保存權限使用者列表
		List<Role> savedRoles = roleRepo.saveAll(roles);
		savedRoles.forEach(System.out::println);

		// ----- 初始化使用者 -----
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
		String fileName = "users.json";

		// 獲取當前的JSON檔案的陣列
		JSONArray root = JsonParserUtils.readFile(fileName);

		// 獲得資料庫的角色資料
		for (int i = 0; i < root.length(); i++) {

			// 獲取當前的JSON對象
			JSONObject object = root.getJSONObject(i);
			System.out.println("object: " + object);

			// 解析地址
			List<Address> addresses = new ArrayList<>();
			Address address = gson.fromJson(object.getJSONObject("address").toString(), Address.class);
			addresses.add(address);

			// 設置密碼
			String pwd = object.getString("password");
			System.out.println("password: " + pwd);

			// 創建購物車對象
			Cart cart = new Cart();
			

			// 轉換JSON對象為User對象
			User user = gson.fromJson(object.toString(), User.class);
			user.setAddresses(addresses);
			user.setPassword(passwordEncoder.encode(pwd));
			user.setCart(cart);

			System.out.println("user: " + user);

			cart.setUser(user);
			
			// 保存使用者對象
			userRepo.save(user);
			cartRepo.save(cart);
			createService.createUser(user);
		}
		
	}
}
