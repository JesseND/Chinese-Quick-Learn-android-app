/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * {@link WordsFragment} that displays a list of words.
 */
public class WordsFragment extends Fragment {

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
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    public WordsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word(R.string.word_sun,
                R.string.chinese_word_sun, R.drawable.word_teacher,R.raw.phrase_what_is_your_name));
        words.add(new Word(R.string.word_rain,
                R.string.chinese_word_rain, R.drawable.word_teacher,R.raw.phrase_my_name_is));
        words.add(new Word(R.string.word_temperature,
                R.string.chinese_word_temperature, R.drawable.word_teacher,R.raw.phrase_how_are_you_feeling));
        words.add(new Word(R.string.word_moon,
                R.string.chinese_word_moon, R.drawable.word_teacher,R.raw.phrase_im_feeling_good));
        words.add(new Word(R.string.word_cloud,
                R.string.chinese_word_cloud,R.drawable.word_teacher, R.raw.phrase_are_you_coming));
        words.add(new Word(R.string.word_picture,
                R.string.chinese_word_picture, R.drawable.word_teacher,R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_date,
                R.string.chinese_word_date,R.drawable.word_teacher, R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_forest,
                R.string.chinese_word_forest,R.drawable.word_teacher, R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_flower,
                R.string.chinese_word_flower, R.drawable.word_teacher,R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_tree,
                R.string.chinese_word_tree,R.drawable.word_teacher, R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_animal,
                R.string.chinese_word_animal,R.drawable.word_teacher, R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_fish,
                R.string.chinese_word_fish, R.drawable.word_teacher,R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_dog,
                R.string.chinese_word_dog,R.drawable.word_teacher,R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_horse,
                R.string.chinese_word_horse, R.drawable.word_teacher,R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_chicken,
                R.string.chinese_word_chicken, R.drawable.word_teacher,R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_cow,
                R.string.chinese_word_cow, R.drawable.word_teacher,R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_monkey,
                R.string.chinese_word_monkey, R.drawable.word_teacher,R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_pig,
                R.string.chinese_word_pig, R.drawable.word_teacher,R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_building,
                R.string.chinese_word_building,R.drawable.word_teacher, R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_bank,
                R.string.chinese_word_bank,R.drawable.word_teacher, R.raw.phrase_what_do_you_do_as_profession));
        words.add(new Word(R.string.word_hospital,
                R.string.chinese_word_hospital,R.drawable.word_teacher, R.raw.phrase_lets_go));
        words.add(new Word(R.string.word_school,
                R.string.chinese_word_school,R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_office,
                R.string.chinese_word_office,R.drawable.word_teacher, R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_classroom,
                R.string.chinese_word_classroom,R.drawable.word_teacher, R.raw.phrase_what_do_you_do_as_profession));
        words.add(new Word(R.string.word_room,
                R.string.chinese_word_room, R.drawable.word_teacher,R.raw.phrase_lets_go));
        words.add(new Word(R.string.word_bus,
                R.string.chinese_word_bus, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_church,
                R.string.chinese_word_church, R.drawable.word_teacher,R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_playground,
                R.string.chinese_word_playground, R.drawable.word_teacher,R.raw.phrase_what_do_you_do_as_profession));
        words.add(new Word(R.string.word_sport,
                R.string.chinese_word_sport,R.drawable.word_teacher, R.raw.phrase_lets_go));
        words.add(new Word(R.string.word_football,
                R.string.chinese_word_football,R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_basketball,
                R.string.chinese_word_basketball, R.drawable.word_teacher ,R.raw.phrase_where_are_you_going));
        words.add(new Word(R.string.word_volleyball,
                R.string.chinese_word_volleyball,R.drawable.word_teacher, R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.word_tennis,
                R.string.chinese_word_tennis,R.drawable.word_teacher, R.raw.phrase_what_do_you_do_as_profession));
        words.add(new Word(R.string.word_jogging,
                R.string.chinese_word_jogging,R.drawable.word_teacher, R.raw.phrase_lets_go));
        words.add(new Word(R.string.word_car,
                R.string.chinese_word_car, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_motorbike,
                R.string.chinese_word_motorbike, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_bicycle,
                R.string.chinese_word_bicycle,R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_plane,
                R.string.chinese_word_plane, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_truck,
                R.string.chinese_word_truck,R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_ship,
                R.string.chinese_word_ship, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_crossroads,
                R.string.chinese_word_crossroads, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_speed,
                R.string.chinese_word_speed, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_fast,
                R.string.chinese_word_fast,R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_slow,
                R.string.chinese_word_slow, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_big,
                R.string.chinese_word_big,R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_small,
                R.string.chinese_word_small, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_thick,
                R.string.chinese_word_thick, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_thin,
                R.string.chinese_word_thin,R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_tall,
                R.string.chinese_word_tall, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_short,
                R.string.chinese_word_short, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_message,
                R.string.chinese_word_message, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_key,
                R.string.chinese_word_key, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_student,
                R.string.chinese_word_student, R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_teacher,
                R.string.chinese_word_teacher,  R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_colleague,
                R.string.chinese_word_colleague, R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_fruit,
                R.string.chinese_word_fruit, R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_apple,
                R.string.chinese_word_apple, R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_banana,
                R.string.chinese_word_banana, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_pineapple,
                R.string.chinese_word_pineapple, R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_mango,
                R.string.chinese_word_mango, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_food,
                R.string.chinese_word_food,R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_vegetable,
                R.string.chinese_word_vegetable,R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_meat,
                R.string.chinese_word_meat, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_beer,
                R.string.chinese_word_beer, R.drawable.word_teacher,R.raw.phrase_come_here));
        words.add(new Word(R.string.word_milk,
                R.string.chinese_word_milk,R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_juice,
                R.string.chinese_word_juice,R.drawable.word_teacher, R.raw.phrase_come_here));
        words.add(new Word(R.string.word_water,
                R.string.chinese_word_water,R.drawable.word_teacher, R.raw.phrase_come_here));


        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

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
                Word word = words.get(position);

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
