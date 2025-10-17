package com.campus.wallet.exception;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException(Integer storeId) {
        super("Store not found with storeId: " + storeId);
    }
}
