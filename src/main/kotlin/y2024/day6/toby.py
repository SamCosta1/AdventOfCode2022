import time
import numpy as np
from enum import Enum

st = time.time()
total = 0

class direction(Enum):
    up = [0, -1]
    left = [1, 0]
    down = [0, 1]
    right = [-1, 0]

class Arrow():
    def __init__(self, x, y, dir: direction):
        self.x = x
        self.y = y
        self.dir = dir

    def moveArrow(self):
        x = self.x + self.dir.value[0]
        y = self.y + self.dir.value[1]

        return Arrow(x, y, self.dir)

    def coords(self):
        return (self.x, self.y)

    def turnArrow(self):
        if self.dir == direction.up:
            self.dir = direction.left

        elif self.dir == direction.left:
            self.dir = direction.down

        elif self.dir == direction.down:
            self.dir = direction.right

        elif self.dir == direction.right:
            self.dir = direction.up

        return self

def createDummyList(list):
    newList = []

    for i in list:
        newList.append(i)

    return newList

def findCharater(lines):
    hashes = set()
    arrow = []
    max = len(lines)

    for j, line in enumerate(lines):
        i = line.find('#', 0)

        while i != -1:
            hashes.add((i, j))
            i = line.find('#', i + 1)

        i2 = line.find('^')
        if i2 != -1:
            arrow = Arrow(i2, j, direction.up)

    return hashes, arrow, max

def ifInfinite(hashes, arrow: Arrow, max, obstruction):
    steps = set()

    while arrow.x >= 0 and arrow.x <= max and arrow.y >= 0 and arrow.y <= max:
        if (arrow.x, arrow.y, arrow.dir) in steps:
            return obstruction

        moveArrow = arrow.moveArrow()
        if moveArrow.coords() in hashes or moveArrow.coords() == obstruction:
            arrow.turnArrow()
        else:
            steps.add((arrow.x, arrow.y, arrow.dir))
            arrow = moveArrow

    return None

def moveGuard(hashes, arrow: Arrow, max):
    startArrow = arrow
    obstruction = set()
    progress, total = 0, 0

    while arrow.x + arrow.dir.value[0] >= 0 and arrow.x + arrow.dir.value[0] <= max - 1 and arrow.y + arrow.dir.value[1] >= 0 and arrow.y + arrow.dir.value[1] <= max - 1:
        if arrow.moveArrow().coords() in hashes:
            arrow.turnArrow()

        else:
            if arrow.moveArrow().coords() not in hashes and arrow.moveArrow().coords() not in obstruction:
                isObstr = ifInfinite(hashes, Arrow(startArrow.x, startArrow.y, startArrow.dir), max, arrow.moveArrow().coords())

                if isObstr is not None:
                    obstruction.add(isObstr)

            arrow = arrow.moveArrow()

        progress += 1

        if progress % 100 == 0:
            print(f"{progress} / 5641")

    return len(obstruction)

with open("src/main/kotlin/y2024/day6/data.txt", "r") as file:
    lines = [line.rstrip() for line in file]

    hashes, arrow, max = findCharater(lines)
    total = moveGuard(hashes, arrow, max)

print(total)
# get the end time
et = time.time()

# get the execution time
elapsed_time = et - st
print('Execution time:', elapsed_time, 'seconds')