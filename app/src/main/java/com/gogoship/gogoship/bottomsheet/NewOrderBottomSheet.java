package com.gogoship.gogoship.bottomsheet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gogoship.gogoship.MainActivity;
import com.gogoship.gogoship.R;
import com.gogoship.gogoship.fragment.DashboardFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;

public class NewOrderBottomSheet extends BottomSheetDialogFragment {

    String string;
    String prod_name="",order_note="",color="",prod_link="",prod_quant="",prod_size="",user_id="",from_where="",to_where="",ship_type="",insurance_check="",language_key="",prod_photo="";
    File photo1=null;
    ConstraintLayout keep_shopping;
    DashboardFragment dashboardFragment;
    public static NewOrderBottomSheet newInstance(String string) {
        NewOrderBottomSheet f = new NewOrderBottomSheet();
        Bundle args = new Bundle();
        args.putString("string", string);
        f.setArguments(args);
        return f;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        //setStyle(DialogFragment.STYLE_NO_FRAME,0);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_order_bottom_sheet, container, false);
        TextView tv = (TextView) v.findViewById(R.id.text);
        keep_shopping=v.findViewById(R.id.cons_keep_shpping);
        /*Bundle mArgs = getArguments();
        String myValue = mArgs.getString("key");
        prod_name = mArgs.getString("product_name");
        order_note = mArgs.getString("order_note");
        color = mArgs.getString("color");
        prod_link = mArgs.getString("prod_link");
        prod_quant = mArgs.getString("prod_quantity");
        prod_size = mArgs.getString("prod_size");
        user_id = mArgs.getString("user_id");
        from_where = mArgs.getString("from_where");
        to_where = mArgs.getString("to_where");
        ship_type = mArgs.getString("ship_type");
        insurance_check = mArgs.getString("insurance_check");
        language_key = mArgs.getString("language_key");
        prod_photo = mArgs.getString("photo");
        photo1= new File(prod_photo);
        Log.e("check","       "+prod_name+"      "+order_note+"        "+color+"        "+prod_link+"         "+prod_quant+"          "+prod_size+"          "+user_id+"         "+from_where+"        "+to_where+"       "+ship_type+"         "+insurance_check+"         "+language_key);*/
        //dialog cancel when touches outside (Optional)
        dashboardFragment=new DashboardFragment();
        shoppingListener();
        getDialog().setCanceledOnTouchOutside(true);

        return v;
    }

    private void shoppingListener() {
        keep_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("login_key", "login_value");
                startActivity(intent);
            }
        });
    }
}
