package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.utils.DepartmentAdapter;
import com.example.myapplication.utils.request.DepartmentData;
import com.example.myapplication.utils.retrofit.ApiCall;
import com.example.myapplication.utils.retrofit.BaseRetrofitApi;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private DepartmentAdapter adapters;
    private List<DepartmentData> departmentList;
    ActivityMainBinding mainBinding;
    private Spinner departmentSpinner;
    private CharSequence positionValue;
    public String getAllDeptValue;

    /*
        List<UserData> dataList = new ArrayList<>();
    public static final String TAG = MainActivity.class.getCanonicalName();
    //pagination
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(mainBinding.getRoot());
        generateAllData();
        mainBinding.wirteFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateAllData();
            }

        });

        mainBinding.readFilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readDataFromInternalStorage(mainBinding.result);
            }
        });


        /*
                ApiCall call = BaseRetrofitApi.getUrlApiCall().create(ApiCall.class);
        Call<List<UserData>> userDataCall = null;
        userDataCall = call.getAllData();

        userDataCall.enqueue(new Callback<List<UserData>>() {
            @Override
            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                generateData(response.body());
            }

            @Override
            public void onFailure(Call<List<UserData>> call, Throwable t) {

            }
        });
         */

    }

    private void generateAllData() {

        ApiCall call = BaseRetrofitApi.getUrlApiCall().create(ApiCall.class);
        Call<List<DepartmentData>> departmentData = null;
        departmentData = call.getDepartmentData();
        departmentData.enqueue(new Callback<List<DepartmentData>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<DepartmentData>> call, Response<List<DepartmentData>> response) {
                List<DepartmentData> data = response.body();

                if (response.body() != null) {


                    Gson gson = new Gson();
                    List<DepartmentData> departmentData1 = new ArrayList<>();
                    departmentData1.addAll(response.body());
                    String convertJsonVal = gson.toJson(departmentData1);
                    writeDepartmentFile(convertJsonVal);


                } else {
                    Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<List<DepartmentData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void writeDepartmentFile(String nameOfTheDepartment) {
        try {
            File file = new File(getApplicationContext().getFilesDir(), "mySampleText.txt");

            if (file.exists()) {
                boolean result = file.delete();

                if (result) {

                    FileOutputStream outputStream = openFileOutput("mySampleText.txt", MODE_PRIVATE);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    outputStreamWriter.write(nameOfTheDepartment);
                    outputStreamWriter.close();
                    Toast.makeText(MainActivity.this, "File Save Successfully", Toast.LENGTH_SHORT).show();
                    System.out.println("Created successfully" + getFilesDir().getAbsolutePath() + "/mySampleText.txt");

                }

            } else {

                Toast.makeText(MainActivity.this, "sorry, there is no such file is exists.", Toast.LENGTH_SHORT).show();

                FileOutputStream outputStream = openFileOutput("mySampleText.txt", MODE_PRIVATE);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                outputStreamWriter.write(nameOfTheDepartment);
                outputStreamWriter.close();
                Toast.makeText(MainActivity.this, "File Save Successfully", Toast.LENGTH_SHORT).show();
                System.out.println("Created successfully" + getFilesDir().getAbsolutePath() + "/mySampleText.txt");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readDataFromInternalStorage(TextView textView) {
        try {

            File file = new File(getFilesDir(), "mySampleText.txt");

            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            getAllDeptValue = sb.toString();

            departmentSpinner = mainBinding.departmentSp;
            departmentList = readDepartmentData(MainActivity.this, getAllDeptValue);
            positionValue = "1";
            adapters = new DepartmentAdapter(MainActivity.this, departmentList);
            adapters.getFilter().filter(positionValue);
            departmentSpinner.setAdapter(adapters);

            textView.setText(sb.toString());

            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            Log.i(TAG, "readDataFromInternalStorage: " + e.getMessage());
        }

    }

    public List<DepartmentData> readDepartmentData(Context context, String FILE_NAME) {
        List<DepartmentData> departmentDataList = null;
        try {
            try {
                departmentDataList = new ArrayList<>();
                Gson gson = new Gson();
                JsonArray jsonArray = new JsonParser().parse(FILE_NAME).getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonElement str = jsonArray.get(i);
                    DepartmentData obj = gson.fromJson(str, DepartmentData.class);
                    departmentDataList.add(obj);
                    System.out.println(obj);
                    System.out.println(str);
                    System.out.println("-------");
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return departmentDataList;
    }

    /*private void generateData(List<UserData> data){

        UserAdapters adapters = new UserAdapters(MainActivity.this, data);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        mainBinding.uiRecyclerView.setLayoutManager(manager);
        mainBinding.uiRecyclerView.setAdapter(adapters);

        mainBinding.uiRecyclerView.addOnScrollListener(new PaginationScrollListener(manager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
//                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
//        loadFirstPage();

    }

     */
}