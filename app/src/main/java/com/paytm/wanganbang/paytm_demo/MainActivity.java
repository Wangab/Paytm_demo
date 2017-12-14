package com.paytm.wanganbang.paytm_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchantapp);
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    //This is to refresh the order id: Only for the Sample App's purpose.
    @Override
    protected void onStart(){
        super.onStart();
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


	/*private void initOrderId() {
		Random r = new Random(System.currentTimeMillis());
		String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
				+ r.nextInt(10000);
		EditText orderIdEditText = (EditText) findViewById(R.id.order_id);
		orderIdEditText.setText(orderId);
	}*/

    public void onStartTransaction(View view) {
        PaytmPGService Service = PaytmPGService.getStagingService();

        //Kindly create complete Map and checksum on your server side and then put it here in paramMap.

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", "SHAREI49948047950647");
        paramMap.put("ORDER_ID", "ORDER0000000001");
        paramMap.put("CUST_ID", "100009881112");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", "1.01");
        paramMap.put("WEBSITE", "APP_STAGING");
        paramMap.put("CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
        paramMap.put("EMAIL", "wangcl@ushareit.com");
        paramMap.put("MOBILE_NO", "7777777777");
        paramMap.put("CHECKSUMHASH", "JDPcD6bS3Dz4xnO0z32FllCJpGLm+yj0oahZAqW30Ogq89KnveNP8gNddL9rKOaN3I0LIvS80YVTMtd8LOXK/Paa/1qJAZ94q1h/IlB0gfs=");
        PaytmOrder Order = new PaytmOrder(paramMap);


        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {

                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction : " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction response "+inResponse.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() {
                        // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }
    /*Button bt_paytm = null;
    Button bt_order = null;
    PaytmOrder order = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_order = findViewById(R.id.button_order);
        bt_order.setOnClickListener((view -> {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("MID", "SHAREI49948047950647");
            paramMap.put("ORDER_ID", "ORDER0000000001");
            paramMap.put("CUST_ID", "100009881112");
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", "1.01");
            paramMap.put("WEBSITE", "APP_STAGING");
            paramMap.put("CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
            paramMap.put("EMAIL", "wangcl@ushareit.com");
            paramMap.put("MOBILE_NO", "7777777777");
            paramMap.put("CHECKSUMHASH", "JDPcD6bS3Dz4xnO0z32FllCJpGLm+yj0oahZAqW30Ogq89KnveNP8gNddL9rKOaN3I0LIvS80YVTMtd8LOXK/Paa/1qJAZ94q1h/IlB0gfs=");
            order = new PaytmOrder(paramMap);
            String order_info = new JSONObject(paramMap).toString();
            Toast.makeText(getApplicationContext(), order_info, Toast.LENGTH_LONG).show();
        }));

        bt_paytm = findViewById(R.id.button_paytm);
        bt_paytm.setOnClickListener((View view) -> {
            Toast.makeText(getApplicationContext(), "Paytm 支付", Toast.LENGTH_SHORT).show();
            PaytmPGService service = PaytmPGService.getProductionService();
            this.onStartTransaction(service);
        });

    }

    public void onStartTransaction(PaytmPGService service) {
        if (order == null) {
            Toast.makeText(getApplicationContext(), "订单未生成，请先生成订单", Toast.LENGTH_SHORT).show();
        } else {
            service.initialize(order, null);
            service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
                @Override
                public void onTransactionResponse(Bundle bundle) {
                    Toast.makeText(getApplicationContext(), "onTransactionResponse ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void networkNotAvailable() {
                    Toast.makeText(getApplicationContext(), "networkNotAvailable", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void clientAuthenticationFailed(String s) {
                    Toast.makeText(getApplicationContext(), "clientAuthenticationFailed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void someUIErrorOccurred(String s) {
                    Toast.makeText(getApplicationContext(), "someUIErrorOccurred", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorLoadingWebPage(int i, String s, String s1) {
                    Toast.makeText(getApplicationContext(), "onErrorLoadingWebPage", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBackPressedCancelTransaction() {
                    Toast.makeText(getApplicationContext(), "onBackPressedCancelTransaction", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onTransactionCancel(String s, Bundle bundle) {
                    Toast.makeText(getApplicationContext(), "onTransactionCancel", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }*/
}
