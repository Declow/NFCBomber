package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import dk.sdu.mmmi.ap.g14.nfcbomber.database.DbHelper;
import dk.sdu.mmmi.ap.g14.nfcbomber.database.objects.HighscoreItem;

public class ScoreboardActivity extends AppCompatActivity {

    private RecyclerView recylerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        recylerView = (RecyclerView) findViewById(R.id.highscore_list);
        recylerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recylerView.setLayoutManager(mLayoutManager);

        mAdapter = new HighscoreAdapter(readDb());

        recylerView.setAdapter(mAdapter);
    }

    /**
     * Reads the scoresboard from the local database
     *
     * @return HighscoreItem[]
     */
    private HighscoreItem[] readDb() {
        DbHelper helper = new DbHelper(this.getApplicationContext());
        ArrayList<HighscoreItem> items = helper.readDb();
        Collections.reverse(items);
        helper.close();

        for (HighscoreItem item : items) {
            System.out.println("bombtime: " + item.getDateOfScore());
        }

        return items.toArray(new HighscoreItem[items.size()]);
    }
}
