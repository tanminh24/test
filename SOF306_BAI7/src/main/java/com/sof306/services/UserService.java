package com.sof306.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sof306.entities.Account;
import com.sof306.repositories.AccountRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	AccountRepository accountRepo;
	BCryptPasswordEncoder pe = new BCryptPasswordEncoder();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Account account = accountRepo.findById(username).get();
			String password = account.getPassword();
			String[] roles = account.getAuthorities().stream().map(au -> au.getRole().getId())
					.collect(Collectors.toList()).toArray((new String[0]));
			return User.withUsername(username).password(pe.encode(password)).roles(roles).build();
		} catch (Exception e) {
			throw new UsernameNotFoundException(username + " not found!");
		}
	}

}
