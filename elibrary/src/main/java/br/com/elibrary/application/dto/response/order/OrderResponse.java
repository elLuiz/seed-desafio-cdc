package br.com.elibrary.application.dto.response.order;

import br.com.elibrary.model.order.Address;
import br.com.elibrary.model.order.Order;

public record OrderResponse(String addressInfo, String complement, String city, String zipCode, String country, String state) {
    public static OrderResponse toResponse(Order order) {
        Address address = order.getAddress();
        String name = address.getState() == null ? null : address.getState().getName();
        return new OrderResponse(address.getAddressInfo(), address.getComplement(), address.getCity(), address.getZipCode(), address.getCountry().getName(), name);
    }
}