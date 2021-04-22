'use strict'

const POINT_RADIUS = 5

// 0 - setting points; 1 - setting query rect; 3 - needs restart
let stage = 0
let proceedBtnText = ['Preprocessing', 'Run']
let mainCanvas


function drawPoint(context, color, point) {
    context.fillStyle = color
    context.beginPath()
    context.arc(point.x, point.y, POINT_RADIUS, 0, 2 * Math.PI)
    context.fill()
}

function drawGrid(canvas) {
    let context = canvas.getContext('2d')
    context.strokeStyle = window.getComputedStyle(canvas).getPropertyValue('--grid-color')
    context.lineWidth = 0.5

    for (let i = 10; i < canvas.width; i += 10) {
        context.beginPath()
        context.moveTo(i, 10)
        context.lineTo(i, canvas.height - 10)
        context.stroke()
    }

    for (let i = 10; i < canvas.height; i += 10) {
        context.beginPath()
        context.moveTo(10, i)
        context.lineTo(canvas.width - 10, i)
        context.stroke()
    }
}

function drawGraph(canvas, points, queryRect) {
    let context = canvas.getContext('2d')
    canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height)
    drawGrid(canvas)
    for (let point of points) {
        drawPoint(context, window.getComputedStyle(canvas).getPropertyValue('--point-color'), point)
    }
    if (queryRect != null) {
        //todo: draw queryRect
    }
}

function onCanvasClick(e) {
    let x = e.pageX - mainCanvas.offsetLeft
    let y = e.pageY - mainCanvas.offsetTop
    let point = points.find(item => Math.abs(x - item.x) < POINT_RADIUS && Math.abs(y - item.y) < POINT_RADIUS)
    switch (stage) {
        case 0:
            if (point) {
                showMessage(`point already exists`, `warning`)
            } else {
                point = new Point(x, y)
                points.push(point)
                showMessage(`added point (${point.x}, ${point.y})`, `log`)
            }
            drawGraph(mainCanvas, points)
            break
        case 1:
            point = new Point(x, y)
            if (queryRect.left_up == null) {
                queryRect.left_up = point
                showMessage(`left-up point of region: (${point.x}, ${point.y})`, `log`)
                drawGraph(mainCanvas, points, queryRect)
            } else if (queryRect.right_bottom == null){
                queryRect.right_bottom = point
                showMessage(`right-bottom point of region: (${point.x}, ${point.y})`, `log`)
                drawGraph(mainCanvas, points, queryRect)
                document.getElementById('proceed-button').classList.add('active-button')
            }
            break
        default:
            break
    }
}

function proceed() {
    let proceedBtn = document.getElementById('proceed-button')
    if (!proceedBtn.classList.contains('active-button')) {
        return
    }
    switch (stage) {
        case 0:
            //todo: build binary tree for algorithm

            stage++
            proceedBtn.classList.remove('active-button')
            proceedBtn.innerText = proceedBtnText[stage]
            showMessage(`select rectangle and run`, `tip`)
            break
        case 1:
            //todo: get result

            stage++
            proceedBtn.classList.remove('active-button')
            showMessage(`found ${0} points`, `info`)
            showMessage(`reset to restart`, `tip`)
            break
        default:
            break
    }

}

function reset() {
    points = []
    queryRect.reset()
    stage = 0
    drawGraph(mainCanvas, points)
    let proceedBtn = document.getElementById('proceed-button')
    proceedBtn.innerText = proceedBtnText[stage]
    if (!proceedBtn.classList.contains('active-button')) {
        proceedBtn.classList.add('active-button')
    }
}

function showMessage(msg, className) {
    let console = document.getElementById('console')
    let newMessage = document.createElement('p')
    newMessage.classList.add(className)
    newMessage.innerText = `[${className}] ${msg}`
    console.appendChild(newMessage)
    console.scroll(0, console.scrollHeight)
}

addEventListener('load', () => {
    mainCanvas = document.getElementById('main-canvas')
    mainCanvas.width = mainCanvas.parentElement.clientWidth * 0.7
    mainCanvas.height = 500
    mainCanvas.addEventListener('click', onCanvasClick)

    document.getElementById('proceed-button').addEventListener('click', proceed)
    document.getElementById('reset-button').addEventListener('click', () => {
        showMessage('Canvas cleared', 'info')
        reset()
    })
    reset()
})

