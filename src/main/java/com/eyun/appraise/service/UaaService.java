package com.eyun.appraise.service;

import org.springframework.web.bind.annotation.GetMapping;

import com.eyun.appraise.client.AuthorizedFeignClient;
import com.eyun.appraise.service.dto.UserDTO;

@AuthorizedFeignClient(name="uaa")
public interface UaaService {

	@GetMapping("/api/account")
	public UserDTO getAccount();
	
}
