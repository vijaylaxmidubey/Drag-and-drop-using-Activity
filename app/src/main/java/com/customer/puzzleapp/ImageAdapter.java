package com.customer.puzzleapp;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> implements DraggableItemAdapter<ImageAdapter.MyViewHolder>  {
    ArrayList<Bitmap> chunkedImages ;
    private int mLastRemovedPosition = -1;

   private boolean isMatched = false;
    private List<Bitmap>originalData;
    ImageMatchedListener listener;
    private int mItemMoveMode = RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT;
    ImageAdapter(ImageMatchedListener listener, ArrayList<Bitmap> chunkedImages){
        this.chunkedImages=chunkedImages;
        this.listener=listener;
        originalData   = new LinkedList<>();

        setHasStableIds(true);
        for(int i=0;i<chunkedImages.size();i++){

            final long id = chunkedImages.size();
            final int viewType = 0;

            final int swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_SWIPE_UP | RecyclerViewSwipeManager.REACTION_CAN_SWIPE_DOWN;
            originalData.add(chunkedImages.get(i));

        }
        Collections.shuffle(originalData);

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.image_adapter, parent, false);
        return new MyViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

       // chunkedImages.get(position);
        holder.imageView.setImageBitmap(originalData.get(position));

    }

    @Override
    public int getItemCount() {
        return originalData.size();
    }

    @Override
    public boolean onCheckCanStartDrag(@NonNull MyViewHolder holder, int position, int x, int y) {
        return true;
    }

    @Nullable
    @Override
    public ItemDraggableRange onGetItemDraggableRange(@NonNull MyViewHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if ((fromPosition == toPosition) || isMatched) {
            // DraggableGridExampleAdapter draggableGridExampleAdapter=new DraggableGridExampleAdapter();
            return;
        }

        Collections.swap(originalData,fromPosition,toPosition);
        if(checkImage()){
            System.out.println("Image Matched!");
            isMatched = true;
            listener.onImageMatched();

        }else{
            System.out.println("Image Matched Not!");
        }
        mLastRemovedPosition = -1;
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public void onItemDragStarted(int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        notifyDataSetChanged();
    }

    public class MyViewHolder extends AbstractDraggableItemViewHolder {
        public ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }

    private boolean checkImage(){
        for(int i=0;i<originalData.size();i++){
            if(originalData.get(i) != originalData.get(i))
                return false;
        }
        return true;
    }

}
