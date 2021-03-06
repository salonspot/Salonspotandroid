package com.scet.saloonspot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scet.saloonspot.R;
import com.scet.saloonspot.models.Request;
import com.scet.saloonspot.utils.Constant;

import java.util.ArrayList;

public class AppoitmentAdapater extends RecyclerView.Adapter<AppoitmentAdapater.ViewHolder> {
    Context context;
    ArrayList<Request> list = new ArrayList<>();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    String loginKey;

    public AppoitmentAdapater(Context context, ArrayList<Request> list, String loginKey) {
        this.context = context;
        this.list = list;
        this.loginKey = loginKey;
    }

    @NonNull
    @Override
    public AppoitmentAdapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_appo_card, null, false);
        return new AppoitmentAdapater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Request request = list.get(position);

        holder.txtamount.setText(request.getAmount());
        holder.txtTime.setText(request.getAppointment_Time());
        holder.status.setText(request.getStatus());

        holder.rvServices.setLayoutManager(new LinearLayoutManager(context));
        ServiceAdapter adapter = new ServiceAdapter(context, request.getService(), "aap", null);
        adapter.notifyDataSetChanged();
        holder.rvServices.setAdapter(adapter);

        if (loginKey.equalsIgnoreCase(Constant.USER)) {
            holder.llOptions.setVisibility(View.GONE);
            holder.txtuser.setText(request.getSaloon().getName());
        } else {
            if (request.getStatus().equalsIgnoreCase("Confirm")) {
                holder.btnDecline.setVisibility(View.GONE);
                holder.btnConfirm.setText("Confirmed");
                holder.txtuser.setText(request.getUser().getUserName());
            } else if(request.getStatus().equalsIgnoreCase("Pending")) {

                holder.llOptions.setVisibility(View.VISIBLE);
                holder.btnDecline.setVisibility(View.VISIBLE);
                holder.btnConfirm.setVisibility(View.VISIBLE);
                holder.txtuser.setText(request.getUser().getUserName());
            }else
            {
                holder.btnConfirm.setVisibility(View.GONE);
                holder.btnDecline.setText("Declined");

//                holder.llOptions.setVisibility(View.VISIBLE);
                holder.txtuser.setText(request.getUser().getUserName());
            }
        }

        holder.btnConfirm.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        request.setStatus("Confirm");
        mDatabase.child("R_Request").child(request.getUserID()).child(request.getKey()).setValue(request);

        holder.btnDecline.setVisibility(View.GONE);
        holder.btnConfirm.setText("Confirmed");
    }
    });

        holder.btnDecline.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        request.setStatus("Decline");
        mDatabase.child("R_Request").child(request.getUserID()).child(request.getKey()).setValue(request);
        holder.btnConfirm.setVisibility(View.GONE);
        holder.btnDecline.setText("Declined");
    }
    });
}

    @Override
    public int getItemCount() {
        return list.size();
    }


public class ViewHolder extends RecyclerView.ViewHolder {

    TextView txtuser, txtamount, txtTime, status;
    RecyclerView rvServices;
    Button btnConfirm, btnDecline;
    LinearLayout llOptions;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        txtuser = itemView.findViewById(R.id.user);
        txtamount = itemView.findViewById(R.id.amount);
        txtTime = itemView.findViewById(R.id.time);
        rvServices = itemView.findViewById(R.id.rvServices);
        status = itemView.findViewById(R.id.status);
        btnConfirm = itemView.findViewById(R.id.btnConfirm);
        btnDecline = itemView.findViewById(R.id.btnDecline);
        llOptions = itemView.findViewById(R.id.llOption);
    }
}
}
