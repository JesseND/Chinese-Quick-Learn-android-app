package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link VerbsFragment} that displays a list of verbs
 *
 */
public class VerbsFragment extends Fragment {

    /** Handles playback of all the sound files */
    private MediaPlayer mMediaPlayer;

    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mediaPlayer){
            releaseMediaPlayer();
        }
    };

    public VerbsFragment(){
        //Required empty Constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.word_list,container, false);

        //Create and setup {@link AudioManager}to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //Create a list of words
        final ArrayList<Word> verb = new ArrayList<Word>();
        verb.add(new Word(R.string.verb_speak,R.string.chinese_verb_speak,R.raw.phrase_where_are_you_going));
        verb.add(new Word(R.string.verb_read,
                R.string.chinese_verb_read, R.raw.phrase_what_is_your_name));
        verb.add(new Word(R.string.verb_listen,
                R.string.chinese_verb_listen, R.raw.phrase_my_name_is));
        verb.add(new Word(R.string.verb_write, R.string.chinese_verb_write, R.raw.phrase_how_are_you_feeling));
        verb.add(new Word(R.string.verb_eat,
                R.string.chinese_verb_eat, R.raw.phrase_im_feeling_good));
        verb.add(new Word(R.string.verb_drink,
                R.string.chinese_verb_drink, R.raw.phrase_are_you_coming));
        verb.add(new Word(R.string.verb_buy,
                R.string.chinese_verb_buy, R.raw.phrase_yes_im_coming));
        verb.add(new Word(R.string.verb_sell,
                R.string.chinese_verb_sell, R.raw.phrase_what_do_you_do_as_profession));
        verb.add(new Word(R.string.verb_drive,
                R.string.chinese_verb_drive, R.raw.phrase_lets_go));
        verb.add(new Word(R.string.verb_ride,
                R.string.chinese_verb_ride, R.raw.phrase_come_here));
        verb.add(new Word(R.string.verb_study,R.string.chinese_verb_study,R.raw.phrase_where_are_you_going));
        verb.add(new Word(R.string.verb_work,
                R.string.chinese_verb_work, R.raw.phrase_my_name_is));
        verb.add(new Word(R.string.verb_teach,
                R.string.chinese_verb_teach, R.raw.phrase_im_feeling_good));
        verb.add(new Word(R.string.verb_walk,
                R.string.chinese_verb_walk, R.raw.phrase_what_is_your_name));
        verb.add(new Word(R.string.verb_run, R.string.chinese_verb_run, R.raw.phrase_how_are_you_feeling));
        verb.add(new Word(R.string.verb_wear,
                R.string.chinese_verb_wear, R.raw.phrase_are_you_coming));
        verb.add(new Word(R.string.verb_sit,
                R.string.chinese_verb_sit, R.raw.phrase_yes_im_coming));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(getActivity(), verb, R.color.primary_color);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        // Set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();

                // Get the {@link Word} object at the given position the user clicked on
                Word word = verb.get(position);

                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
