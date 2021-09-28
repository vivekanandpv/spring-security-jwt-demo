package com.example.springsecurityjwtdemo.viewmodels;

import java.util.List;

public class ProcessCompletionViewModel {
    private boolean isCompleted;
    private List<String> errors;
    private String message;

    public ProcessCompletionViewModel(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public ProcessCompletionViewModel(boolean isCompleted, List<String> errors, String message) {
        this.isCompleted = isCompleted;
        this.errors = errors;
        this.message = message;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
