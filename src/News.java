public class News {
    private String date;
    private String title;
    private String author;
    private String content;

    public News(String date, String title, String author, String content) {
        this.date = date;
        this.title = title;
        this.author = author;
        this.content = content;
    }

    // Геттеры и сеттеры
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Date: " + date + "\n" +
                "Title: " + title + "\n" +
                "Author: " + author + "\n" +
                "Content: " + content + "\n";
    }
}
