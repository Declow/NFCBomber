package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import dk.sdu.mmmi.ap.g14.nfcbomber.database.objects.HighscoreItem;

public class HighscoreAdapter extends RecyclerView.Adapter<HighscoreAdapter.ViewHolder> {
    private HighscoreItem[] dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date_field;
        public TextView your_time;
        public TextView bomb_time;
        public TextView difference_time;

        public ViewHolder(View view) {
            super(view);
            date_field = (TextView) view.findViewById(R.id.listitem_date);
            your_time = (TextView) view.findViewById(R.id.listitem_yourtime);
            bomb_time = (TextView) view.findViewById(R.id.listitem_bombtime);
            difference_time = (TextView) view.findViewById(R.id.listitem_difference);
        }
    }

    public HighscoreAdapter(HighscoreItem[] items) {
        dataset = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_highscore, parent, false);
        ViewHolder vh = new ViewHolder(layout);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(dataset[position].getDateOfScore());
        holder.date_field.setText(date);
        holder.your_time.setText("");
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }
}
