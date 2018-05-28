package e.macbookpro.mycart;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import e.macbookpro.mycart.model.Product;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Product> productList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView productNameTxt,productPriceTxt, quantityTxt;
        public ImageView btnAdd, btnReduce, btnRemove, prouductImage;
        public ViewHolder(View v) {
            super(v);
            productNameTxt = v.findViewById(R.id.nameProducts);
            productPriceTxt = v.findViewById(R.id.priceProduct);
            quantityTxt = v.findViewById(R.id.quantityTxt);
            btnAdd = v.findViewById(R.id.btnAdd);
            btnReduce = v.findViewById(R.id.btnReduce);
            btnRemove = v.findViewById(R.id.btnRemove);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)


    public MyAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item_description, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productNameTxt.setText(productList.get(position).getProductName());
        holder.productPriceTxt.setText("Rp, "+productList.get(position).getProductPrice()); //Kasih thousand separator
        
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "BUTTON ADD", Toast.LENGTH_SHORT).show();
            }
        });
        
        holder.btnReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "BUTTON REDUCE", Toast.LENGTH_SHORT).show();
            }
        });
        
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "BUTTON REMOVE", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return productList.size();
    }
}