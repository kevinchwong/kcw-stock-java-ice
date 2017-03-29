package com.stocks;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Table(name = "stocks")
public class Stock {

	@Id
	@GeneratedValue
	int id;

	String symbol;

	long quantity;

	public Stock() {
	}

	public Stock(String symbol, long quantity) {
		this.symbol = symbol;
		this.quantity = quantity;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int x) {
		this.id = x;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String x) {
		this.symbol = x;
	}

	public long getQuantity() {
		return this.quantity;
	}

	public void setQuantity(long x) {
		this.quantity = x;
	}

}
