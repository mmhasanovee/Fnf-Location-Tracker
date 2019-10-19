package xyz.mmhasanovee.fnflocationtracker.ViewHolder;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.mmhasanovee.fnflocationtracker.Interface.IRecycItemListerner;
import xyz.mmhasanovee.fnflocationtracker.R;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public CircleImageView recycler_profile_image;
   public TextView txt_user_email;
     IRecycItemListerner iRecycItemListerner;

    public void setiRecycItemListerner(IRecycItemListerner iRecycItemListerner) {
        this.iRecycItemListerner = iRecycItemListerner;
    }

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        recycler_profile_image=(CircleImageView) itemView.findViewById(R.id.recycler_profile_image);
        txt_user_email = (TextView) itemView.findViewById(R.id.txt_user_email);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        iRecycItemListerner.onItemClickListener(view,getAdapterPosition());

    }
}
