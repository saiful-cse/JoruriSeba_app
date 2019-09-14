package com.coxtunes.joruriseba.Log_in;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coxtunes.joruriseba.Contacts;
import com.coxtunes.joruriseba.R;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class AdminContact_adapter extends RecyclerView.Adapter<AdminContact_adapter.EachContactView>{
    Context ctx;
    List<Contacts> contactsList;


    public AdminContact_adapter(Context ctx, List<Contacts> contactsList) {
        this.ctx = ctx;
        this.contactsList = contactsList;
    }


    @NonNull
    @Override
    public EachContactView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.each_contact_admin_view,null);
        return new EachContactView(view,contactsList);
    }

    @Override
    public void onBindViewHolder(@NonNull EachContactView holder, int position) {
        final Contacts contacts = contactsList.get(position);

        holder.name.setText(contacts.getName());
        holder.area_chamber_shop.setText(contacts.getArea_chamber_shop());
        holder.phone.setText(contacts.getPhone());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AdminContactsUpdateDelete.class);
                intent.putExtra("id", contacts.getId());
                intent.putExtra("name", contacts.getName());
                intent.putExtra("phone", contacts.getPhone());
                intent.putExtra("area_chamber_shop", contacts.getArea_chamber_shop());
                intent.putExtra("service_name", contacts.getService_name());
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    class EachContactView extends RecyclerView.ViewHolder{
        List<Contacts> contactsListView;

        TextView name, area_chamber_shop, phone;
        ImageView imageView;

        private EachContactView(View itemView, final List<Contacts> contacts) {
            super(itemView);

            contactsListView = contactsList;

            name = itemView.findViewById(R.id.name);
            area_chamber_shop = itemView.findViewById(R.id.area_chamber_shop);
            phone = itemView.findViewById(R.id.phone);
            imageView = itemView.findViewById(R.id.navigation_right);

        }
    }
}
