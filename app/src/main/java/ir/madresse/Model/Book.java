package ir.madresse.Model;

/**
 * Created by Lincoln on 18/05/16.
 */
public class Book {
    private String name;
    private String ImageUrl;
    private String PdfUrl;





    public Book(String name,String ImageUrl,String PdfUrl) {
        this.name = name;
        this.ImageUrl = ImageUrl;
        this.PdfUrl=PdfUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
    public String getPdfUrl() {
        return PdfUrl;
    }

    public void setImageUrl(String numOfSongs) {
        this.ImageUrl = ImageUrl;
    }
    public void setPdfUrl(String numOfSongs) {
        this.ImageUrl = ImageUrl;
    }


}
