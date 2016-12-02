package com.tackpad.dao;


import com.tackpad.models.Token;

public interface TokenDao extends BaseDao<Token> {
	Token findByValue(String value);
}
