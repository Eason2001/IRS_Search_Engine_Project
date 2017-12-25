package com.uwindsor.eason.lucenesearch.indexObjects;


public class Article {
    private int docId;     //id in the Index Base
    private String fileId; //id in the original docs folder, reserved for the future
    private String title;
    private String author;
    private String year;
    private String abstracttxt;
    private String content;
    private String publisher;
    private String category;
    private String filepath;
    private float score;
    private int totalhits;

    public Article() {

    }

    public Article(int docId, String title, String author, String year) {
        this.docId = docId;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAbstracttxt() {
        return abstracttxt;
    }

    public void setAbstracttxt(String abstracttxt) {
        this.abstracttxt = abstracttxt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getTotalhits() {
        return totalhits;
    }

    public void setTotalhits(int totalhits) {
        this.totalhits = totalhits;
    }

    @Override
    public String toString() {
        return "Artical [docId=" + docId + ", title=" + title + ", score=" + score + ", filepath="
                + filepath + "]";
    }

}
