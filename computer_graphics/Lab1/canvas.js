'use strict'

let mainCanvas
let polygonFinished = false
let pointSet = false
let simple

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
    let context = mainCanvas.getContext('2d');
    if (!polygonFinished) {
        let color = '#598add'

        drawPoint(context, color, point)
        if (points.length > 0) {
            drawLine(context, color, points[points.length - 1], point)
        }

        points.push(point)

        showMessage(`Set #${points.length} point at (${point.x}, ${point.y})`, 'log')
        let proceedBtn = document.getElementById('proceed-button')
        if (points.length === 2) {
            proceedBtn.classList.add('active-button')
        }
    } else if (!pointSet && simple) {
        let color = '#00c0bc'
        drawPoint(context, color, point)
        pointToCheck = point
        pointSet = true
        document.getElementById('proceed-button').classList.add('active-button')
        showMessage(`Set point to check at (${point.x}, ${point.y})`, 'log')
    }
}

function proceed() {
    if (!polygonFinished && points.length > 2) {
        let color = '#598add'
        drawLine(mainCanvas.getContext('2d'), color, points[0], points[points.length - 1])
        polygonFinished = true
        this.innerText = 'Run'
        simple = isSimple(points)
        showMessage('Polygon closed', 'info')
        if (!simple) {
            showMessage('Polygon is not simple', 'warning')
            showMessage('Reset recommended', 'tip')
        } else {
            showMessage('Set point to check', 'tip')
        }
    } else if (pointSet) {
        let context = mainCanvas.getContext('2d');
        let color = '#d8364b'

        context.strokeStyle = color
        context.beginPath()
        context.moveTo(10, pointToCheck.y)
        context.lineTo(mainCanvas.width - 10, pointToCheck.y)
        context.stroke()

        let res = isInsidePolygon(points, pointToCheck)
        for (let point of res.crossPoints) {
            drawPoint(context, color, point)
        }

        if (res.result) {
            showMessage('Point is inside the polygon', 'info')
            showMessage('Reset to restart', 'tip')
        } else {
            showMessage('Point is outside the polygon', 'info')
            showMessage('Reset to restart', 'tip')
        }
    }
    this.classList.remove('active-button')
}

function reset() {
    points = []
    mainCanvas.getContext('2d').clearRect(0, 0, mainCanvas.width, mainCanvas.height)
    let proceedBtn = document.getElementById('proceed-button')
    proceedBtn.innerText = 'Close polygon'
    if (proceedBtn.classList.contains('active-button')) {
        proceedBtn.classList.remove('active-button')
    }
    polygonFinished = false
    pointSet = false
    showMessage('Add at least 3 points', 'tip')
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

