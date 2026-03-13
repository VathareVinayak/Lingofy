package com.example.translate_objecttext;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Edits {

    @SerializedName("end")
    @Expose
    private Integer end;
    @SerializedName("error_type")
    @Expose
    private String errorType;
    @SerializedName("general_error_type")
    @Expose
    private String generalErrorType;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("replacement")
    @Expose
    private String replacement;
    @SerializedName("sentence")
    @Expose
    private String sentence;
    @SerializedName("sentence_start")
    @Expose
    private Integer sentenceStart;
    @SerializedName("start")
    @Expose
    private Integer start;


    public Edits(Integer end, String errorType, String generalErrorType, String id, String replacement, String sentence, Integer sentenceStart, Integer start) {
        this.end = end;
        this.errorType = errorType;
        this.generalErrorType = generalErrorType;
        this.id = id;
        this.replacement = replacement;
        this.sentence = sentence;
        this.sentenceStart = sentenceStart;
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getGeneralErrorType() {
        return generalErrorType;
    }

    public void setGeneralErrorType(String generalErrorType) {
        this.generalErrorType = generalErrorType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public Integer getSentenceStart() {
        return sentenceStart;
    }

    public void setSentenceStart(Integer sentenceStart) {
        this.sentenceStart = sentenceStart;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

}
