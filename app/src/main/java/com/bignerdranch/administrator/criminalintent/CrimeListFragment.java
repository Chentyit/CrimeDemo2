package com.bignerdranch.administrator.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerview;
    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerview = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerview.setAdapter(mAdapter);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;

        private CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));

            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
        }

        private void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }

        public void onClick(View view) {
            Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 新建一个专门给警察Police的Holder，其实和CrimeHolder差不多
     */
    private class CrimePoliceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Button mConnect;
        private Crime mCrime;

        private CrimePoliceHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime_police, parent, false));

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title_police);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date_police);
            mConnect = (Button) itemView.findViewById(R.id.connect_police);
            mConnect.setOnClickListener(this);
        }

        private void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }

        public void onClick(View view) {
            Toast.makeText(getActivity(), "已联系警察", Toast.LENGTH_SHORT).show();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        /**
         * 这里最重要，获取当前item是否需要联系警察
         */
        @Override
        public int getItemViewType(int position) {
            int flag = 0;
            if (mCrimes.get(position).isRequiresPolice()){
                flag = 1;
            }
            return flag;
        }

        /**
         * getItemViewType的返回值返回到这里的viewType里面，通过判断viewType的值判断添加什么布局
         */
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if (viewType == 0) {
                return new CrimeHolder(layoutInflater, parent);
            } else {
                return new CrimePoliceHolder(layoutInflater, parent);
            }
        }

        /**
         * 这里就是直接添加控件的标题，功能等
         */
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CrimeHolder) {
                Crime crime = mCrimes.get(position);
                ((CrimeHolder) holder).bind(crime);
            } else if (holder instanceof CrimePoliceHolder){
                Crime crime = mCrimes.get(position);
                ((CrimePoliceHolder) holder).bind(crime);
            }
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}
