package com.coxtunes.joruriseba;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Contact_adapter extends RecyclerView.Adapter<Contact_adapter.EachContactView>{
    Context ctx;
    List<Contacts> contactsList;


    public Contact_adapter(Context ctx, List<Contacts> contactsList) {
        this.ctx = ctx;
        this.contactsList = contactsList;
    }


    @NonNull
    @Override
    public EachContactView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.each_contact_view,null);
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

                android.support.v7.app.AlertDialog.Builder aleart1 = new android.support.v7.app.AlertDialog.Builder(ctx);
                aleart1.setCancelable(true);
                aleart1.setMessage("আপনি কি "+contacts.getName()+" কে কল করতে চান?");
                aleart1.setIcon(R.drawable.alert);

                aleart1.setPositiveButton("হা", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "+88"+contacts.getPhone(), null));
                        ctx.startActivity(intent);
                    }
                });

                aleart1.setNegativeButton("না", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                android.support.v7.app.AlertDialog dlg = aleart1.create();
                dlg.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    class EachContactView extends RecyclerView.ViewHolder{
        List<Contacts> contactsViewHolder;

        TextView name, area_chamber_shop, phone;
        ImageView imageView;

        public EachContactView(View itemView, final List<Contacts> contacts) {
            super(itemView);

            contactsViewHolder = contacts;
            name = itemView.findViewById(R.id.name);
            area_chamber_shop = itemView.findViewById(R.id.area_chamber_shop);
            phone = itemView.findViewById(R.id.phone);
            imageView = itemView.findViewById(R.id.phoneIcon);
        }
    }
}
