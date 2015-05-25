package com.victorsystems.zodiacal;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.victorsystems.zodiacal.data.AuxiliarFunctions;
import com.victorsystems.zodiacal.fragments.DetailFragment;
import com.victorsystems.zodiacal.sync.ZodiacalSyncAdapter;
import com.victorsystems.zodiacal.fragments.HoroscopeFragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends ActionBarActivity implements HoroscopeFragment.Callback {
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;
    private String mPrincipalSigno;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrincipalSigno = AuxiliarFunctions.getPersonalSign(this);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.signos_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.signos_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }

        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        HoroscopeFragment horoscopeFragment = ((HoroscopeFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_horoscope));
        horoscopeFragment.setUseSpecialLayout(!mTwoPane);

        ZodiacalSyncAdapter.initializeSyncAdapter(this);

        //Banner
        /*AdView mAdView = (AdView) findViewById(R.id.adView);*/
        //Interstitial
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(this.getString(R.string.banner_detail_id));
        //Adding both ad request
        AdRequest adRequest = new AdRequest.Builder().build();
        /*mAdView.loadAd(adRequest);*/
        interstitial.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String signo = AuxiliarFunctions.getPersonalSign(this);
        if (signo != null && !signo.equals(mPrincipalSigno)) {
            HoroscopeFragment hf = (HoroscopeFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_horoscope);
            if (hf != null) {
                hf.onPersonalSignChanged();
            }
            /*DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if (df != null) {
                df.onPersonalSignChanged(signo);
            }*/
            mPrincipalSigno = signo;
        }
    }

    @Override
    public void onItemSelected(Uri signUri) {
        if(interstitial.isLoaded()) {
            interstitial.show();
        } else {
            if(mTwoPane) {
                Bundle args = new Bundle();
                args.putParcelable(DetailFragment.DETAIL_URI, signUri);

                DetailFragment fragment = new DetailFragment();
                fragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.signos_detail_container, fragment, DETAILFRAGMENT_TAG)
                        .commit();
            } else {
                Intent intent = new Intent(this, DetailActivity.class).setData(signUri);
                startActivity(intent);
            }
        }

    }





}
