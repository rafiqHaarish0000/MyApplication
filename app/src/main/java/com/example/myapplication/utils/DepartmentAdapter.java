package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.utils.request.DepartmentData;

import java.util.ArrayList;
import java.util.List;

public class DepartmentAdapter extends BaseAdapter {
    List<DepartmentData> data;
    public List<DepartmentData> SubjectListTemp;
    public List<DepartmentData> MainList;
    public SubjectDataFilter subjectDataFilter;
    Context context;

    public DepartmentAdapter(Context context, List<DepartmentData> data) {
        this.context = context;
        this.data = data;
        this.SubjectListTemp = new ArrayList<DepartmentData>();
        this.SubjectListTemp.addAll(data);
        this.MainList = new ArrayList<DepartmentData>();
        this.MainList.addAll(data);
    }


    public Filter getFilter() {

        if (subjectDataFilter == null) {

            subjectDataFilter = new SubjectDataFilter();
        }
        return subjectDataFilter;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class SubjectDataFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charParentID) {

            charParentID = charParentID.toString().toLowerCase();


            FilterResults filterResults = new FilterResults();

            if (charParentID != null && charParentID.toString().length() > 0) {
                ArrayList<DepartmentData> arrayList1 = new ArrayList<DepartmentData>();

                for (int i = 0, l = MainList.size(); i < l; i++) {
                    DepartmentData subject = MainList.get(i);

                    String valueOfParet = String.valueOf(subject.getREVSION_NO());

                    if (valueOfParet.equals(charParentID))

                        arrayList1.add(subject);
                }
                filterResults.count = arrayList1.size();

                filterResults.values = arrayList1;
            } else {
                synchronized (this) {
                    filterResults.values = MainList;

                    filterResults.count = MainList.size();
                }
            }
            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            SubjectListTemp = (ArrayList<DepartmentData>) filterResults.values;

            notifyDataSetChanged();

            data.clear();

            for (int i = 0, l = SubjectListTemp.size(); i < l; i++)
                data.add(SubjectListTemp.get(i));

            //subjectDataFilter.notifyDataSetInvalidated();
        }

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater lInflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);

            view = lInflater.inflate(R.layout.main_list_view, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.data_values);
        textView.setText(data.get(i).getNAME());
//        textView.setVisibility(View.GONE);
        return view;
    }
}

