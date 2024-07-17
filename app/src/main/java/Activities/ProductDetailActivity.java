package Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.stores.R;
import com.example.stores.databinding.ActivityProductDetailBinding;
import com.example.stores.databinding.LayoutProductDetailBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import Adapters.SliderAdapterForProductDetail;
import Adapters.ViewPager2Adapter;
import Fragments.DescriptionFragment;
import Fragments.ReviewFragment;
import Fragments.SoldFragment;
import models.Product;
import models.SliderItem;
import models.Store;

public class ProductDetailActivity extends AppCompatActivity {

    private Product object;

    ActivityProductDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        getBundles();
        initSlider();
        setupEvents();
    }

    private void initSlider() {
        ArrayList<SliderItem> sliderItems = new ArrayList<>();
        for (int i = 0; i < object.getPicUrl().size(); i++) {
            sliderItems.add(new SliderItem(object.getPicUrl().get(i)));
        }

        binding.viewPager2Slider.setAdapter(new SliderAdapterForProductDetail(this, sliderItems));
        binding.viewPager2Slider.setOffscreenPageLimit(2);
        binding.indicator.attachTo(binding.viewPager2Slider);



    }

    private void getBundles() {

        ArrayList<Store> listStore = new ArrayList<>();
        listStore.add(new Store("1", "Zozo_Unisex", "TP. Hồ Chí Minh", "https://down-bs-vn.img.susercontent.com/fd234f3899f07b72e9c5e5e26f9d997d_tn.webp"));
        listStore.add(new Store("2", "LOVITO OFFICIAL STORE", "TP. Hồ Chí Minh" , "https://down-bs-vn.img.susercontent.com/f87c39a4a3702cd4cb149cacd8114a0b_tn.webp"));
        listStore.add(new Store("3", "SANDAshop.vn", "Hà Nội", "https://down-bs-vn.img.susercontent.com/ac5556f336029ae92a1058195f2d4e56_tn.webp"));

        object = (Product) getIntent().getSerializableExtra("object");
        if (object != null){

            // setup product information
            binding.txtTitle.setText(object.getProductName());
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedOldPrice = formatter.format(object.getOldPrice());
            binding.txtOldPrice.setText("đ" + formattedOldPrice);
            binding.txtOldPrice.setPaintFlags(binding.txtOldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            String formattedPrice = formatter.format(object.getNewPrice());
            binding.txtPrice.setText(formattedPrice);
            binding.ratingBar.setRating(4.5F);
            binding.txtRating.setText(4.5 + " / 5");
            binding.txtSold.setText("Đã bán " + 200);
//            binding.txtProdctDescription.setText(object.getDescription());

            // setup store information

            Store store = null;
            for (Store s: listStore){
//                if (s.getStoreID() == object.getStoreID()){
//                    store = s;
//                    break;
//                }
            }
            binding.txtStoreName.setText(store.getStoreName());
            binding.txtStoreAddress.setText(store.getStoreAddress());

            try {

                Glide.with(this).load(store.getStoreImage()).into(binding.imvStoreImage);
            } catch (Exception exception) {
                Log.e("ERROR", "Không tải được hình ảnh", exception);
            }


        }


    }

    private void setupEvents(){
        binding.btnAddToCart.setOnClickListener(v -> {


        });
        binding.imageBack.setOnClickListener(v -> finish());

        binding.iconCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(intent);
        });

        binding.txtProductDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpProductDetailDialog();
            }
        });
    }

    private void popUpProductDetailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutProductDetailBinding detailBinding = LayoutProductDetailBinding.inflate(getLayoutInflater());
        builder.setView(detailBinding.getRoot());
        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_edit_text_border);
        dialog.show();

        // Set the dialog window attributes
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);

        }

        detailBinding.imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        detailBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }




    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#F04D7F"));

        getWindow().setNavigationBarColor(Color.parseColor("#EFEFEF"));
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}