'use strict'

let mainCanvas
let polygonFinished = false
let pointSet = false

function drawPoint(context, color, point) {
    context.fillStyle = color
    context.beginPath()
    context.arc(point.x, point.y, 3, 0, 2 * Math.PI)
    context.fill()
}

function drawLine(context, color, start, end) {
    context.strokeStyle = color
    context.beginPath()
    context.moveTo(start.x, start.y)
    context.lineTo(end.x, end.y)
    context.stroke()
}


function onCanvasClick(e) {
    let point = new Point(e.pageX - mainCanvas.offsetLeft, e.pageY - mainCanvas.offsetTop)
    if (!polygonFinished) {
        let color = '#598add'
        let context = mainCanvas.getContext('2d');

        drawPoint(context, color, point)

        if (points.length > 0) {
            drawLine(context, color, points[points.length - 1], point)
        }

        points.push(point)
    } else {

    }
}

function proceed() {
    if (!polygonFinished) {
        let color = '#598add'
        drawLine(mainCanvas.getContext('2d'), color, points[0], points[points.length - 1])
        polygonFinished = true
        if (isSimple(points)) {
            this.classList.remove('active-button')
        } else {

        }
    } else {

    }
}

function reset() {
    points = []
    mainCanvas.getContext('2d').clearRect(0, 0, mainCanvas.width, mainCanvas.height)
    let proceedBtn = document.getElementById('proceed-button')
    if (!proceedBtn.classList.contains('active-button')) {
        proceedBtn.classList.add('active-button')
    }
    polygonFinished = false
    pointSet = false
}

addEventListener('load', () => {
    mainCanvas = document.getElementById('main-canvas')
    mainCanvas.width = mainCanvas.parentElement.clientWidth
    mainCanvas.height = 500
    mainCanvas.addEventListener('click', onCanvasClick)

    document.getElementById('proceed-button').addEventListener('click', proceed)
    document.getElementById('reset-button').addEventListener('click', reset)
})

