package com.tfg.springvaadin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("utilService")
@Transactional
public class UtilServiceImpl implements UtilService{
	
	@Override
	public Boolean isNullorEmpty(String s) {
		Boolean resultado = false;
		if(("").equals(s) || s==null) {
			resultado = true;
		}
		
		return resultado;
	}

}
