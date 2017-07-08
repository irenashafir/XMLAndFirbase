package shafir.irena.xmlandfirbase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;
// TODO: Delete all but onCreateView
/**
 * A fragment representing a list of Items.
 */
public class YnetArticleFragment extends Fragment implements YnetDataSource.onYnetArrivedListener {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_ynetarticle_list, container, false);

        YnetDataSource.getYnet(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return recyclerView;
    }


    @Override
    public void onYnetArrived(final List<YnetDataSource.Ynet> data, final Exception e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e == null){
                    recyclerView.setAdapter(new YnetAdapter(data, getActivity()));

                }
                else {
                    Toast.makeText(getContext(), "pls check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    static class YnetAdapter extends RecyclerView.Adapter<YnetAdapter.YnetViewHolder>{
        // create ViewHolder
        // getCount
        // bind view holder to the data
        // properties:

        private List<YnetDataSource.Ynet> data;
        private Context context;
        private LayoutInflater inflater;

        public YnetAdapter(List<YnetDataSource.Ynet> data, Context context) {
            this.data = data;
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }



        @Override
        public YnetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // inflate a single item --> View
            View v = inflater.inflate(R.layout.ynet_item, parent, false);
            // return a new view holder
            return new YnetViewHolder(v);
        }

        @Override
        public void onBindViewHolder(YnetViewHolder holder, int position) {
            YnetDataSource.Ynet ynet = data.get(position);
            holder.tvTitle.setText(ynet.getTitle());
            holder.tvContent.setText(ynet.getDescription());
            Picasso.with(context).load(ynet.getImage()).into(holder.ivThumbnail);
        }


        @Override
        public int getItemCount() {
            return data.size();
        }


        class YnetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            // View Holder- holds the view
            TextView tvContent;
            TextView tvTitle;
            ImageView ivThumbnail;


            public YnetViewHolder(View itemView) {
                super(itemView);

                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvContent = (TextView) itemView.findViewById(R.id.tvContent);
                ivThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (context instanceof AppCompatActivity) {
                    FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();

                    int position = getAdapterPosition();
                    YnetDataSource.Ynet ynet = data.get(position);

                    YnetDetailFragment detailFragment =YnetDetailFragment.newInstance(ynet.getLink());
                    fm.beginTransaction().replace(R.id.frame, detailFragment)
                            .addToBackStack("details").commit();
                }
            }
        }


    }

}
