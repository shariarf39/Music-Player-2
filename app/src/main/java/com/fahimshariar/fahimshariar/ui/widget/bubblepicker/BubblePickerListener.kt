package com.fahimshariar.fahimshariar.ui.widget.bubblepicker

import com.ldt.fahimshariar.ui.widget.bubblepicker.model.PickerItem

/**
 * Created by irinagalata on 3/6/17.
 */
interface BubblePickerListener {

    fun onBubbleSelected(item: PickerItem, position: Int)

    fun onBubbleDeselected(item: PickerItem, position: Int)

}