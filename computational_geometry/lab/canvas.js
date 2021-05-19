'use strict'

const POINT_RADIUS = 4

//0 - drawing A
//1 - drawing B
//2 - moving
//3 - morph
//4 - needs restart
let stage = 0;
let proceedBtnText = ['finish A', 'finish B', 'morph', '...', '...']
let mainCanvas
let proceedBtn

let colors = {
    point: window.getComputedStyle(mainCanvas).getPropertyValue('--point-color'),
    edgeA: window.getComputedStyle(mainCanvas).getPropertyValue('--edge-color-A'),
    edgeB: window.getComputedStyle(mainCanvas).getPropertyValue('--edge-color-B'),
    morph: window.getComputedStyle(mainCanvas).getPropertyValue('--morph-color'),
    grid: window.getComputedStyle(mainCanvas).getPropertyValue('--grid-color'),
}

function drawPoint(context, point, color) {
    context.fillStyle = color
    context.beginPath()
    context.arc(point.x, point.y, POINT_RADIUS, 0, 2 * Math.PI)
    context.fill()
}

function drawGrid(canvas, color) {
    let context = canvas.getContext('2d')
    context.strokeStyle = color
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

function drawPolygon(canvas, points, pointColor, edgeColor) {
    if (points.length === 0) {
        return
    }

    let context = canvas.getContext('2d')
    context.strokeStyle = edgeColor
    context.lineWidth = 1

    context.beginPath()
    context.moveTo(points[0].x, points[0].y)
    points.forEach(point => context.lineTo(point.x, point.y))
    context.lineTo(points[0].x, points[0].y)
    context.stroke()

    points.forEach(point => drawPoint(context, point, pointColor))
}

function drawAll() {
    drawGrid(mainCanvas, colors.grid)
    drawPolygon(mainCanvas, pointsA, colors.point, colors.edgeA)
    drawPolygon(mainCanvas, pointsB, colors.point, colors.edgeB)
    drawPolygon(mainCanvas, morphDst, colors.morph, colors.morph)
}

// function onCanvasClick(e) {
//     let x = e.pageX - mainCanvas.offsetLeft
//     let y = e.pageY - mainCanvas.offsetTop
//
//     switch (stage) {
//         case 0:
//             if (pointsA.find(item => Math.abs(x - item.x) < POINT_RADIUS && Math.abs(y - item.y) < POINT_RADIUS)) {
//                 showMessage(`point already exists`, `warning`)
//             } else {
//                 let point = new Point(x, y)
//                 pointsA.push(point)
//                 showMessage(`added point (${point.x}, ${point.y})`, `log`)
//             }
//             drawAll();
//             break
//         case 1:
//             if (pointsB.find(item => Math.abs(x - item.x) < POINT_RADIUS && Math.abs(y - item.y) < POINT_RADIUS)) {
//                 showMessage(`point already exists`, `warning`)
//             } else {
//                 let point = new Point(x, y)
//                 pointsB.push(point)
//                 showMessage(`added point (${point.x}, ${point.y})`, `log`)
//             }
//             drawAll();
//             break;
//         default:
//             break;
//
//     }
//
//
// }
//
//
// function proceed() {
//     if (!proceedBtn.classList.contains('active-button')) {
//         return
//     }
//
//     switch (stage) {
//         case 0:
//             break;
//         case 1:
//             break;
//         case 2:
//             break;
//         case 3:
//             break;
//     }
//
//
//     done = true
//     drawAll
//
//
//     proceedBtn.classList.remove('active-button')
//     showMessage(`reset to restart`, `tip`)
//
//
//     let color = '#598add'
//     drawLine(mainCanvas.getContext('2d'), color, points[0], points[points.length - 1])
//     polygonFinished = true
//     this.innerText = 'Run'
//     simple = isSimple(points)
//     showMessage('Polygon closed', 'info')
//     if (!simple) {
//         showMessage('Polygon is not simple', 'warning')
//         showMessage('Reset recommended', 'tip')
//     } else {
//         showMessage('Set point to check', 'tip')
//     }
// }

function reset() {
    pointsA = []
    pointsB = []
    morphSrc = []
    morphDst = []
    stage = 0
    drawAll()

    proceedBtn.innerText = proceedBtnText[stage];
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
    let point
    point = pointsA.find(item => Math.abs(x - item.x) < POINT_RADIUS && Math.abs(y - item.y) < POINT_RADIUS)
    if (!point) {
        point = pointsB.find(item => Math.abs(x - item.x) < POINT_RADIUS && Math.abs(y - item.y) < POINT_RADIUS)
    }

    let hintBox = document.getElementById('hint-box')
    if (point) {
        hintBox.innerText = `x: ${point.x}, y: ${point.y}`
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

    proceedBtn = document.getElementById('proceed-button')
    proceedBtn.addEventListener('click', proceed1)

    document.getElementById('reset-button').addEventListener('click', () => {
        showMessage('Canvas cleared', 'info')
        reset()
    })
    reset()
})

