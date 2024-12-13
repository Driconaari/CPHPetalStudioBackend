package com.flower.shop.cphpetalstudio.dto;

public class ApiResponse {
    private String message;

        // Constructor
        public ApiResponse(String message) {
            this.message = message;
        }

        // Getter
        public String getMessage() {
            return message;
        }

        // Setter
        public void setMessage(String message) {
            this.message = message;
        }
    }

