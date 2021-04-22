'use strict'

let points = []
let queryRect = {
    left_up: null,
    right_bottom: null,
    reset: function () {
        this.left_up = null
        this.right_bottom = null
    }
}

