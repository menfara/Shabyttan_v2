package farkhat.myrzabekov.shabyttan.presentation.ui.decoration

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


open class StartLinearSnapHelper : LinearSnapHelper() {
    private var _verticalHelper: OrientationHelper? = null
    private var _horizontalHelper: OrientationHelper? = null

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        if (layoutManager.canScrollVertically()) {
            return findFirstView(layoutManager, getVerticalHelper(layoutManager))
        } else if (layoutManager.canScrollHorizontally()) {
            return findFirstView(layoutManager, getHorizontalHelper(layoutManager))
        }
        return null
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        val out = IntArray(2) { 0 }
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager))
        } else {
            out[0] = 0
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager))
        } else {
            out[1] = 0
        }
        return out
    }

    private fun findFirstView(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper
    ): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }

        if (layoutManager is LinearLayoutManager) {
            val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
            if (lastVisibleItemPosition == layoutManager.itemCount - 1) return null
        }

        var closestChild: View? = null
        var start: Int = 0

        if (layoutManager.clipToPadding) {
            start = helper.startAfterPadding
        }

        var absClosest = Integer.MAX_VALUE

        for (i in 0 until childCount) {
            val child = layoutManager.getChildAt(i)
            val childStart = helper.getDecoratedStart(child)
            val absDistance = abs(childStart - start)

            if (absDistance < absClosest) {
                absClosest = absDistance
                closestChild = child
            }
        }
        return closestChild
    }

    private fun distanceToStart(targetView: View, helper: OrientationHelper): Int {
        return helper.getDecoratedStart(targetView) - helper.startAfterPadding
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (_verticalHelper == null) {
            _verticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return _verticalHelper!!
    }

    private fun getHorizontalHelper(
        layoutManager: RecyclerView.LayoutManager
    ): OrientationHelper {
        if (_horizontalHelper == null) {
            _horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return _horizontalHelper!!
    }
}