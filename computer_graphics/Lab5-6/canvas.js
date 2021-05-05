'use strict'

const POINT_RADIUS = 5

let done = false;
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

function drawGraph(canvas, points) {
    let context = canvas.getContext('2d')
    context.clearRect(0, 0, canvas.width, canvas.height)
    drawGrid(canvas)
    for (let point of points) {
        drawPoint(context, window.getComputedStyle(canvas).getPropertyValue('--point-color'), point)
    }
}

function drawResult(canvas, points) {
    if (points.length === 0) {
        return
    }

    let context = canvas.getContext('2d')
    context.strokeStyle = window.getComputedStyle(canvas).getPropertyValue('--edge-color')
    context.lineWidth = 1

    context.beginPath()
    context.moveTo(points[0].x, points[0].y)
    points.forEach(point => context.lineTo(point.x, point.y))
    context.lineTo(points[0].x, points[0].y)
    context.stroke()

    points.forEach(point => drawPoint(context, window.getComputedStyle(mainCanvas).getPropertyValue('--highlight-color'), point))
}

function onCanvasClick(e) {
    if (done) {
        return
    }

    let x = e.pageX - mainCanvas.offsetLeft
    let y = e.pageY - mainCanvas.offsetTop
    let point = points.find(item => Math.abs(x - item.x) < POINT_RADIUS && Math.abs(y - item.y) < POINT_RADIUS)

    if (point) {
        showMessage(`point already exists`, `warning`)
    } else {
        point = new Point(x, y)
        points.push(point)
        showMessage(`added point (${point.x}, ${point.y})`, `log`)
    }
    drawGraph(mainCanvas, points)

}

function proceed() {
    let proceedBtn = document.getElementById('proceed-button')
    if (!proceedBtn.classList.contains('active-button')) {
        return
    }

    done = true
    drawGraph(mainCanvas, points)
    drawResult(mainCanvas, QuickHull(points))

    proceedBtn.classList.remove('active-button')
    showMessage(`reset to restart`, `tip`)
}

function reset() {
    points = []
    done = false;
    drawGraph(mainCanvas, points)
    let proceedBtn = document.getElementById('proceed-button')
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

function onMouseMove(e) {
    let x = e.pageX - mainCanvas.offsetLeft
    let y = e.pageY - mainCanvas.offsetTop
    let point = points.find(item => Math.abs(x - item.x) < POINT_RADIUS && Math.abs(y - item.y) < POINT_RADIUS)

    let hintBox = document.getElementById('hint-box')
    if (point) {
        hintBox.innerText = `x: ${mainCanvas.offsetLeft + point.x}, y: ${mainCanvas.offsetTop + point.y}; i: ${points.indexOf(point)}`
        hintBox.style.top = `${e.pageY + 15}px`;
        hintBox.style.left = `${e.pageX + 15}px`;
        hintBox.style.display = 'block'
    } else {
        hintBox.style.display = 'none'
    }
}

addEventListener('load', () => {
    mainCanvas = document.getElementById('main-canvas')
    mainCanvas.width = mainCanvas.parentElement.clientWidth * 0.7
    mainCanvas.height = 500
    mainCanvas.addEventListener('click', onCanvasClick)
    mainCanvas.addEventListener('mousemove', onMouseMove)

    document.getElementById('proceed-button').addEventListener('click', proceed)
    document.getElementById('reset-button').addEventListener('click', () => {
        showMessage('Canvas cleared', 'info')
        reset()
    })
    reset()
})

