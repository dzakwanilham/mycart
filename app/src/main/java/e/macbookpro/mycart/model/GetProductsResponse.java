package e.macbookpro.mycart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProductsResponse {
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
