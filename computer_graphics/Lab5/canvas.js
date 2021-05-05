'use strict'

const POINT_RADIUS = 5

// 0 - setting points; 1 - animating result; 3 - needs restart
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

function drawGraph(canvas, points, queryRect, tree) {
    let context = canvas.getContext('2d')
    context.clearRect(0, 0, canvas.width, canvas.height)
    drawGrid(canvas)
    if (queryRect != null) {
        if (queryRect.left_up && queryRect.right_bottom) {
            context.fillStyle = window.getComputedStyle(canvas).getPropertyValue('--section-color')
            context.fillRect(queryRect.left_up.x, queryRect.left_up.y, queryRect.right_bottom.x - queryRect.left_up.x, queryRect.right_bottom.y - queryRect.left_up.y)
        } else if (queryRect.left_up) {
            drawPoint(context, window.getComputedStyle(canvas).getPropertyValue('--section-color'), queryRect.left_up)
        }
    }
    if (tree) {
        drawTree(mainCanvas, tree)
    }
    for (let point of points) {
        drawPoint(context, window.getComputedStyle(canvas).getPropertyValue('--point-color'), point)
    }
}

function drawTree(canvas, tree) {
    let context = canvas.getContext('2d')
    context.strokeStyle = window.getComputedStyle(canvas).getPropertyValue('--edge-color')
    context.lineWidth = 1

    let drawNode = (node, max_left, max_right, max_top, max_bottom) => {
        if (node == null) {
            return
        }

        context.beginPath()
        if (node.type === "x") {
            context.moveTo(node.point.x, max_top)
            context.lineTo(node.point.x, max_bottom)
        } else {
            context.moveTo(max_left, node.point.y)
            context.lineTo(max_right, node.point.y)
        }
        context.stroke()

        if (node.type === "x") {
            drawNode(node.left, max_left, node.point.x, max_top, max_bottom)
            drawNode(node.right, node.point.x, max_right, max_top, max_bottom)
        } else {
            drawNode(node.right, max_left, max_right, node.point.y, max_bottom)
            drawNode(node.left, max_left, max_right, max_top, node.point.y)

        }
    }

    drawNode(tree, 10, canvas.width - 10, 10, canvas.height - 10)
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
                drawGraph(mainCanvas, points, queryRect, tree)
            } else if (queryRect.right_bottom == null){
                queryRect.right_bottom = point
                showMessage(`right-bottom point of region: (${point.x}, ${point.y})`, `log`)
                drawGraph(mainCanvas, points, queryRect, tree)
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
            tree = createTree(points, null)
            drawGraph(mainCanvas, points, queryRect, tree)

            stage++
            proceedBtn.classList.remove('active-button')
            proceedBtn.innerText = proceedBtnText[stage]
            showMessage(`select rectangle and run`, `tip`)
            break
        case 1:
            let res = []
            search(tree, queryRect, res)
            for (let point of res) {
                drawPoint(mainCanvas.getContext('2d'), window.getComputedStyle(mainCanvas).getPropertyValue('--highlight-color'), point)
            }
            stage++
            proceedBtn.classList.remove('active-button')
            showMessage(`found ${res.length} points`, `info`)
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

