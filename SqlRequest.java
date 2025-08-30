package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SqlRequest {
    @JsonProperty("questionId")
    private String questionId;

    @JsonProperty("finalQuery")
    private String finalQuery;

    // Constructor
    public SqlRequest(String questionId, String finalQuery) {
        this.questionId = questionId;
        this.finalQuery = finalQuery;
    }

    // Getters and setters
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getFinalQuery() {
        return finalQuery;
    }

    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    @Override
    public String toString() {
        return "SqlRequest{" +
                "questionId='" + questionId + '\'' +
                ", finalQuery='" + finalQuery + '\'' +
                '}';
    }
}