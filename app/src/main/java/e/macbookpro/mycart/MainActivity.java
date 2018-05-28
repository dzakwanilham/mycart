package e.macbookpro.mycart;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.util.ArrayList;
import java.util.List;

import e.macbookpro.mycart.model.GetProductsResponse;
import e.macbookpro.mycart.model.Product;
import e.macbookpro.mycart.network.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private RestClient.RestAPI client;
    List<Product> productList = new ArrayList<>();
    List<Product> scannedProduct = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    BeepManager beepManager;
    String lastText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeView = findViewById(R.id.barcodeScanner);
        barcodeView.getBarcodeView().getCameraSettings().setFocusMode(CameraSettings.FocusMode.CONTINUOUS);
        barcodeView.decodeContinuous(callback);
        client = RestClient.getClient();
        beepManager = new BeepManager(this);

        getProductList();

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(scannedProduct);
        mRecyclerView.setAdapter(mAdapter);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                return;
            }

            lastText = result.getText();
            barcodeView.setStatusText(result.getText());

            beepManager.playBeepSoundAndVibrate();

            for (int i = 0; i < productList.size(); i++) {
                if (result.getText().equals(productList.get(i).getProductBarcode())) {
                    barcodeView.setStatusText(productList.get(i).getProductName());
                    scannedProduct.add(productList.get(i));
                    mAdapter.setProductList(scannedProduct);
                }

            }

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    private void getProductList() {
        Call<GetProductsResponse> getProductsResponseCall = client.getProductById();
        getProductsResponseCall.enqueue(new Callback<GetProductsResponse>() {
            @Override
            public void onResponse(Call<GetProductsResponse> call, Response<GetProductsResponse> response) {
                if (response.isSuccessful()) {
                    productList = response.body().getProducts();
                    for (int i = 0; i < response.body().getProducts().size(); i++) {
                        Log.i(TAG, "onResponse: " + response.body().getProducts().get(i).toString());
                    }

                }
            }

            @Override
            public void onFailure(Call<GetProductsResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Gagal mendapatkan data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
