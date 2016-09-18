package apportho.apporthov2;

import android.graphics.Bitmap;

/**
 * Created by guilh on 16/09/2016.
 */
public class ImageItem {
    private Bitmap image;
    private String date;
    private int id;

    public ImageItem(int id, Bitmap image, String date) {
        super();
        this.image = image;
        this.date = date;
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}