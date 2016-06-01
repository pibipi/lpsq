package com.example.db;

public class AddressData {
	private String name;
	private String phone;

	public AddressData(String name, String phone) {
		super();
		this.name = name;
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "AddressData [name=" + name + ", phone=" + phone + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
