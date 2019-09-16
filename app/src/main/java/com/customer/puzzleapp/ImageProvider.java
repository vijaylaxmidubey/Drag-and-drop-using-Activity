package com.customer.puzzleapp;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
public class ImageProvider extends AbstractDataProvider {
    private List<ConcreteData> mData;
    private List<Bitmap>originalData;
    private ImageProvider.ConcreteData mLastRemovedData;
    private int mLastRemovedPosition = -1;
    private DraggableGridExampleAdapter.ImageMatchedListener listener;
    private boolean isMatched = false;
    public ImageProvider(DraggableGridExampleAdapter.ImageMatchedListener listner,List<Bitmap> data) {

        this.originalData = data;
        this.listener = listner;
        mData = new LinkedList<>();

        for(int i=0;i<data.size();i++){

                final long id = mData.size();
                final int viewType = 0;

                final int swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_SWIPE_UP | RecyclerViewSwipeManager.REACTION_CAN_SWIPE_DOWN;
                mData.add(new ImageProvider.ConcreteData(id, viewType, data.get(i), swipeReaction));

        }
        Collections.shuffle(mData);

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Data getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }

        return mData.get(index);
    }

    @Override
    public int undoLastRemoval() {
        if (mLastRemovedData != null) {
            int insertedPosition;
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size()) {
                insertedPosition = mLastRemovedPosition;
            } else {
                insertedPosition = mData.size();
            }

            mData.add(insertedPosition, mLastRemovedData);

            mLastRemovedData = null;
            mLastRemovedPosition = -1;

            return insertedPosition;
        } else {
            return -1;
        }
    }

    @Override
    public void moveItem(int fromPosition, int toPosition) {

        if ((fromPosition == toPosition) || isMatched) {
           // DraggableGridExampleAdapter draggableGridExampleAdapter=new DraggableGridExampleAdapter();
            return;
        }

        // final ConcreteData item = mData.remove(fromPosition);

        //mData.add(toPosition, item);
        Collections.swap(mData,fromPosition,toPosition);
        if(checkImage()){
            System.out.println("Image Matched!");
            isMatched = true;
            listener.onImageMatched();

        }else{
            System.out.println("Image Matched Not!");
        }
        mLastRemovedPosition = -1;
    }
    private boolean checkImage(){
        for(int i=0;i<originalData.size();i++){
            if(originalData.get(i) != mData.get(i).getImage())
                return false;
        }
        return true;
    }

    @Override
    public void swapItem(int fromPosition, int toPosition) {
        if ((fromPosition == toPosition) || isMatched) {
            return;
        }

        Collections.swap(mData, toPosition, fromPosition);
        mLastRemovedPosition = -1;
    }

    @Override
    public void removeItem(int position) {
        //noinspection UnnecessaryLocalVariable
        final ImageProvider.ConcreteData removedItem = mData.remove(position);

        mLastRemovedData = removedItem;
        mLastRemovedPosition = position;
    }

    public static final class ConcreteData extends Data {

        private final long mId;
        @NonNull
        private final Bitmap image;
        private final int mViewType;
        private boolean mPinned;

        ConcreteData(long id, int viewType, @NonNull Bitmap image, int swipeReaction) {
            mId = id;
            mViewType = viewType;
          this.image = image;
        }

        private static String makeText(long id, String text, int swipeReaction) {
            return String.valueOf(id) + " - " + text;
        }

        @Override
        public boolean isSectionHeader() {
            return false;
        }

        @Override
        public int getViewType() {
            return mViewType;
        }

        @Override
        public long getId() {
            return mId;
        }

        @NonNull
        @Override
        public String toString() {
            return "";
        }

        @Override
        public String getText() {
            return "";
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }
        public Bitmap getImage(){
            return image;
        }

        @Override
        public void setPinned(boolean pinned) {
            mPinned = pinned;
        }
    }
}

