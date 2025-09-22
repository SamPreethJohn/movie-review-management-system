package com.sam.movie.moviereview.ExceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle Runtime exceptions(for web pages)
    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException ex, Model model, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("Runtime Error", ex.getMessage(), request.getRequestURI()));
        } else {
            ModelAndView mav = new ModelAndView("error_page");
            mav.addObject("errorMessage", "Something went wrong " + ex.getMessage());
            mav.addObject("url", request.getRequestURI());
            return mav;
        }
    }

    // Handle exception for REST APIs
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgunment(IllegalArgumentException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("Invalid request", ex.getMessage(), request.getRequestURI()));
        } else {
            ModelAndView mav = new ModelAndView("error_page");
            mav.addObject("errorMessage", ex.getMessage());
            mav.addObject("url", request.getRequestURI());
            return mav;
        }
        // }
        // return "{ \"error\": \"" + ex.getMessage() + "\" }";
    } 

    // Generic fallback (catch-all)
    @ExceptionHandler(Exception.class)
    public Object handlerException(Exception ex, Model model, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("Server Error", ex.getMessage(), request.getRequestURI()));
        } else {
            ModelAndView mav = new ModelAndView("error_page");
            mav.addObject("errorMessage", "Unexpected error occured");
            mav.addObject("url", request.getRequestURI());
            return mav;
        }
    }

        // model.addAttribute("errorMessage", "Something went wrong: " + ex.getMessage());
        // model.addAttribute("url", request.getRequestURI());
        // return "error_page";

        private boolean isApiRequest(HttpServletRequest request) {
            return request.getRequestURI().startsWith("/admin/api")
            || request.getRequestURI().startsWith("/api");
        }

        static class ErrorResponse {
            private String error;
            private String message;
            private String path;

            public ErrorResponse(String error, String message, String path) {
                this.error = error;
                this.message = message;
                this.path = path;
            }

            public String getError() {return error; }
            public String getMessage() {return message; }
            public String getPath() {return path; }

        }
}
