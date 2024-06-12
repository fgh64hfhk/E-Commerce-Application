package com.app.utilts;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.app.config.AppConstants;
import com.app.entites.Address;
import com.app.entites.Cart;
import com.app.entites.Role;
import com.app.entites.User;
import com.app.repositories.RoleRepo;
import com.app.repositories.UserRepo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class Initializer {

	@Autowired
	private static RoleRepo roleRepo;
	
	@Autowired
	private static UserRepo userRepo;
	
	@Autowired
	private static PasswordEncoder passwordEncoder;

	public static void create_users() {

		// ----- 初始化使用者 -----
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
		String fileName = "users.json";

		// 獲取當前的JSON檔案的陣列
		JSONArray root = JsonParserUtils.readFile(fileName);

		// 獲得資料庫的角色資料
		Role getUserRole = null;
		if (roleRepo.existsById(AppConstants.USER_ID)) {
			getUserRole = roleRepo.getReferenceById(AppConstants.USER_ID);
		} else {
			System.out.println("not found User_ID");
		}

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

			// 設置角色
			Set<Role> r = new HashSet<Role>();
			r.add(getUserRole);

			// 轉換JSON對象為User對象
			User user = gson.fromJson(object.toString(), User.class);
			user.setAddresses(addresses);
			user.setPassword(passwordEncoder.encode(pwd));
			user.setCart(cart);

			if (getUserRole != null) {
				user.setRoles(r);
			}

			System.out.println("user: " + user);

			// 保存使用者對象
			userRepo.save(user);
		}
	}
}
