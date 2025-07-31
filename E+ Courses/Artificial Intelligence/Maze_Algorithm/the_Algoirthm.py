import tkinter as tk
from tkinter import filedialog, messagebox, ttk
import time
import os
from collections import deque


class MazeData:
    def __init__(self):
        self.walls_h = []  # Stores horizontal walls. Each element in the list representing a row, and boolean values indicate wall presence.
        self.walls_v = []  # Stores vertical walls. Same with horizontal.
        self.entrance = (0, 0)  # Defines the starting coordinates (row, column) of the maze.
        self.exit = (0, 0)  # Defines the exit coordinates (row, column) of the maze.
        self.rows = 0  # Number of rows in the maze grid.
        self.cols = 0  # Number of columns in the maze grid.
        # 'walls_h' and 'walls_v' have sizes of one more row and column than the number of cells in the maze because the walls are located at the edges of the cells.


class Node:
    def __init__(self, row, col, parent=None):
        self.row = row  # Row coordinate of the node.
        self.col = col  # Column coordinate of the node.
        self.parent = parent  # It keeps track of which node this node was reached from. When a solution is found, the path from the input to the output is traced backwards using these parent references. Backtraching.

    def __eq__(self, other):
        # Overrides the equality comparison to check if two nodes have the same row and column.
        return self.row == other.row and self.col == other.col

    def __hash__(self):
        # Overrides the hash function, making Node objects hashable, which is necessary for using them in sets/dictionaries.
        return hash((self.row, self.col))
    # We need eq and hash because they enable node objects to be correctly compared for equality and used in hash-based data structures.
    # They ensures that node objects can be used properly with sets (self.visited) and dictionaries (parent_map).


class MazeSolver:
    def __init__(self, root):
        self.root = root
        self.root.title("Maze Solver")
        self.root.geometry("1000x650")

        self.maze_data = MazeData()
        self.cell_size = 30  # Defines size of cells in GUI.
        self.start_row = 50  # For GUI.
        self.start_col = 50  # For GUI.

        # DFS ve BFS için kullanılacak değişkenler
        self.visited = set()  # Stores visited cells during maze traversal (DFS/BFS).
        self.path = []  # Stores the solved path from entrance to exit.
        self.steps = []  # Stores intermediate steps for step-by-step visualization.
        self.algorithm = "DFS"  # Default algorithm set to DFS.

        # DFS ve BFS karşılaştırma sonuçları
        self.dfs_result = {"path_length": 0, "visited_cells": 0, "time": 0}  # Stores performance metrics for DFS.
        self.bfs_result = {"path_length": 0, "visited_cells": 0, "time": 0}  # Stores performance metrics for BFS.

        self.create_widgets() # For visualization, calls the method that creates all GUI elements.

    def create_widgets(self):
        # Sol panel - Kontroller
        left_panel = tk.Frame(self.root, width=200, height=600)
        left_panel.pack(side=tk.LEFT, fill=tk.Y, padx=10, pady=10)

        # Dosya yükleme
        file_frame = tk.LabelFrame(left_panel, text="Maze File", padx=5, pady=5)
        file_frame.pack(fill=tk.X, pady=10)

        self.file_path_var = tk.StringVar()
        self.file_path_var.set("No file selected")

        file_label = tk.Label(file_frame, textvariable=self.file_path_var, wraplength=180)
        file_label.pack(fill=tk.X)

        load_button = tk.Button(file_frame, text="Select File", command=self.load_maze_file)
        load_button.pack(fill=tk.X, pady=5)

        # Algoritma seçimi
        algo_frame = tk.LabelFrame(left_panel, text="Algorithm", padx=5, pady=5)
        algo_frame.pack(fill=tk.X, pady=10)

        self.algo_var = tk.StringVar(value="DFS")
        tk.Radiobutton(algo_frame, text="DFS", variable=self.algo_var, value="DFS").pack(anchor=tk.W)
        tk.Radiobutton(algo_frame, text="BFS", variable=self.algo_var, value="BFS").pack(anchor=tk.W)

        # Algoritma Çalıştırma
        run_frame = tk.LabelFrame(left_panel, text="Run", padx=5, pady=5)
        run_frame.pack(fill=tk.X, pady=10)

        self.solve_button = tk.Button(run_frame, text="Solve", command=self.solve_maze)
        self.solve_button.pack(fill=tk.X, pady=5)

        self.step_button = tk.Button(run_frame, text="Show Step by Step", command=self.show_steps)
        self.step_button.pack(fill=tk.X, pady=5)

        self.compare_button = tk.Button(run_frame, text="Compare Algorithms", command=self.compare_algorithms)
        self.compare_button.pack(fill=tk.X, pady=5)

        self.clear_button = tk.Button(run_frame, text="Clean", command=self.clear_solution)
        self.clear_button.pack(fill=tk.X, pady=5)

        # Labirent Oluşturma
        create_frame = tk.LabelFrame(left_panel, text="Create Maze", padx=5, pady=5)
        create_frame.pack(fill=tk.X, pady=10)

        tk.Label(create_frame, text="Line:").grid(row=0, column=0, sticky=tk.W)
        self.rows_var = tk.IntVar(value=8)
        tk.Spinbox(create_frame, from_=3, to=20, textvariable=self.rows_var, width=5).grid(row=0, column=1)

        tk.Label(create_frame, text="Column:").grid(row=1, column=0, sticky=tk.W)
        self.cols_var = tk.IntVar(value=8)
        tk.Spinbox(create_frame, from_=3, to=20, textvariable=self.cols_var, width=5).grid(row=1, column=1)

        create_button = tk.Button(create_frame, text="Create New Maze", command=self.create_new_maze)
        create_button.grid(row=2, column=0, columnspan=2, sticky=tk.EW, pady=5)

        save_button = tk.Button(create_frame, text="Save Maze", command=self.save_maze)
        save_button.grid(row=3, column=0, columnspan=2, sticky=tk.EW, pady=5)

        # Sonuçlar
        result_frame = tk.LabelFrame(left_panel, text="Results", padx=5, pady=5)
        result_frame.pack(fill=tk.X, pady=10)

        self.result_text = tk.Text(result_frame, width=20, height=10, wrap=tk.WORD)
        self.result_text.pack(fill=tk.BOTH)

        # Sağ panel - Labirent görünümü
        right_panel = tk.Frame(self.root)
        right_panel.pack(side=tk.RIGHT, fill=tk.BOTH, expand=True, padx=10, pady=10)

        self.canvas = tk.Canvas(right_panel, bg="white")
        self.canvas.pack(fill=tk.BOTH, expand=True)

        # Adım Adım gösterme için kontrol paneli
        self.step_control_frame = tk.Frame(right_panel)
        self.step_prev_button = tk.Button(self.step_control_frame, text="Back", command=self.prev_step)
        self.step_prev_button.pack(side=tk.LEFT, padx=5)

        self.step_counter_var = tk.StringVar(value="0/0")
        self.step_counter_label = tk.Label(self.step_control_frame, textvariable=self.step_counter_var)
        self.step_counter_label.pack(side=tk.LEFT, padx=20)

        self.step_next_button = tk.Button(self.step_control_frame, text="Next", command=self.next_step)
        self.step_next_button.pack(side=tk.LEFT, padx=5)

        # Editör modunda duvar ekleme/kaldırma
        self.canvas.bind("<Button-1>", self.handle_canvas_click)

        # Maze editing state
        self.editing = False  #Boolean flag to indicate if the maze is in editing mode.
        self.current_step = 0  #Current step index for step-by-step visualization.
        self.total_steps = 0  #Total number of steps in the recorded solution.

    # Bu kısım, panelin sol üstündeki maze.txt dosyasını yüklemeye yarıyor.
    def load_maze_file(self):
        file_path = filedialog.askopenfilename(
            filetypes=[("Text files", "*.txt"), ("All files", "*.*")]
        )
        if file_path:
            try:
                self.maze_data = self.parse_maze_file(file_path)
                self.file_path_var.set(os.path.basename(file_path))
                self.draw_maze()
                self.clear_solution()
            except Exception as e:
                messagebox.showerror("Error", f"An error occurred while uploading the file:\n{str(e)}")

    def parse_maze_file(self, file_path):
        # Opens and reads the maze file.
        with open(file_path, 'r') as file:
            lines = [line.rstrip() for line in file.readlines()]

        data = MazeData()

        coord_start_index = 0
        # Iterates through lines to find the starting index of coordinate data (entrance and exit).
        for i, line in enumerate(lines):
            # Checks if the line contains a comma and is composed of digits, indicating coordinates.
            if ',' in line and line.strip().replace(',', '').isdigit():
                coord_start_index = i
                break

        maze_lines = lines[:coord_start_index]  # Extracts lines containing maze wall data.

        # Satır ve sütun sayısını hesapla
        max_line_length = max(len(line) for line in maze_lines)  # Finds the maximum line length to determine columns.
        rows = len(maze_lines) // 2 + 1  # Calculates the number of rows based on horizontal wall lines.
        cols = max_line_length // 2 + 1  # Calculates the number of columns based on characters in a line.

        # Duvar dizilerini oluştur
        h_walls = [[False for _ in range(cols)] for _ in range(rows)]  # Initializes horizontal wall matrix.
        v_walls = [[False for _ in range(cols)] for _ in range(rows)]  # Initializes vertical wall matrix.

        # Duvarları işle
        for i, line in enumerate(maze_lines):
            if i % 2 == 0:  # Processes lines representing horizontal walls.
                row = i // 2
                for j in range(min(len(line), max_line_length)):
                    if line[j] == '-':
                        col = j // 2
                        if col < cols and row < rows:
                            h_walls[row][col] = True
            else:  # Processes lines representing vertical walls.
                row = i // 2
                for j in range(min(len(line), max_line_length)):
                    if line[j] == '|':
                        col = j // 2
                        if col < cols and row < rows:
                            v_walls[row][col] = True

        # Koordinatları oku
        entrance_coords = lines[coord_start_index].strip().split(',')  # Parses entrance coordinates.
        exit_coords = lines[coord_start_index + 1].strip().split(',') if coord_start_index + 1 < len(lines) else ["0", "0"]  # Parses exit coordinates, with a default if missing.

        # Koordinatları kontrol et
        if len(entrance_coords) >= 2 and entrance_coords[0].isdigit() and entrance_coords[1].isdigit():
            data.entrance = (int(entrance_coords[0]), int(entrance_coords[1]))  # Converts entrance coordinates to integers.
        else:
            data.entrance = (0, 0)  # Defaults entrance to (0,0) if parsing fails.

        if len(exit_coords) >= 2 and exit_coords[0].isdigit() and exit_coords[1].isdigit():
            data.exit = (int(exit_coords[0]), int(exit_coords[1]))  # Converts exit coordinates to integers.
        else:
            data.exit = (0, 0)  # Defaults exit to (0,0) if parsing fails.

        # Labirent verilerini ayarla
        data.rows = rows - 1  # Sets the number of rows in the MazeData object.
        data.cols = cols - 1  # Sets the number of columns in the MazeData object.
        data.walls_h = h_walls  # Assigns the parsed horizontal walls.
        data.walls_v = v_walls  # Assigns the parsed vertical walls.

        return data

    def draw_maze(self):

        self.canvas.delete("all")  # Clears all existing drawings on the canvas.

        if not hasattr(self.maze_data, 'rows') or self.maze_data.rows == 0:
            return

        # Labirent boyutları
        rows = self.maze_data.rows
        cols = self.maze_data.cols

        # Canvas boyutunu ayarla
        canvas_width = cols * self.cell_size + 100
        canvas_height = rows * self.cell_size + 100
        self.canvas.config(width=canvas_width, height=canvas_height)  # Adjusts canvas size based on maze dimensions.

        # Dikey duvarları çiz
        for r in range(rows + 1):
            for c in range(cols + 1):
                # Checks if it is within the limits allowed by the system. If valid, checks if there is a vertical wall at that particular location.
                if c < len(self.maze_data.walls_v[0]) and r < len(self.maze_data.walls_v) and self.maze_data.walls_v[r][
                        c]:
                    x = self.start_col + c * self.cell_size
                    y1 = self.start_row + r * self.cell_size
                    y2 = self.start_row + (r + 1) * self.cell_size
                    self.canvas.create_line(x, y1, x, y2, width=2, tags="wall_v")  # Draws a vertical line for the wall.

        # Yatay duvarları çiz
        for r in range(rows + 1):
            for c in range(cols + 1):
                # Checks if it is within the limits allowed by the system. If valid, checks if there is a horizontal wall at that particular location.
                if c < len(self.maze_data.walls_h[0]) and r < len(self.maze_data.walls_h) and self.maze_data.walls_h[r][
                        c]:
                    y = self.start_row + r * self.cell_size
                    x1 = self.start_col + c * self.cell_size
                    x2 = self.start_col + (c + 1) * self.cell_size
                    self.canvas.create_line(x1, y, x2, y, width=2, tags="wall_h")  # Draws a horizontal line for the wall.

        # Giriş noktasını çiz (A harfi)
        entrance_x = self.start_col + self.maze_data.entrance[1] * self.cell_size + self.cell_size // 2
        entrance_y = self.start_row + self.maze_data.entrance[0] * self.cell_size + self.cell_size // 2
        self.canvas.create_text(entrance_x, entrance_y, text="A", font=("Arial", 12, "bold"))

        # Çıkış noktasını çiz (B harfi)
        exit_x = self.start_col + self.maze_data.exit[1] * self.cell_size + self.cell_size // 2
        exit_y = self.start_row + self.maze_data.exit[0] * self.cell_size + self.cell_size // 2
        self.canvas.create_text(exit_x, exit_y, text="B", font=("Arial", 12, "bold"))

    def solve_maze(self):
        if not hasattr(self.maze_data, 'rows') or self.maze_data.rows == 0:
            messagebox.showinfo("Warning", "First load a maze file.")
            return

        self.clear_solution()  # Clears any previously drawn solution or steps.

        # Seçilen algoritmaya göre çöz
        algorithm = self.algo_var.get()
        self.algorithm = algorithm

        start_time = time.time()  # Records the start time for performance measurement.

        if algorithm == "DFS":
            self.dfs_solve()  # Calls the DFS algorithm to solve the maze.
        else:
            self.bfs_solve()  # Calls the BFS algorithm to solve the maze.

        end_time = time.time()  # Records the end time.
        execution_time = end_time - start_time  # Calculates the execution time.

        # Çözüm yolunu çiz
        self.draw_path()  # Draws the found path on the canvas.

        # Sonuçları göster
        result_info = f"Algorithm: {algorithm}\n"
        result_info += f"Path Length: {len(self.path) - 1}\n"  # Path length is number of steps, not nodes.
        result_info += f"Visited Cell: {len(self.visited)}\n"
        result_info += f"Working Hours: {execution_time:.6f} sn"

        # Sonuç bilgilerini kaydet
        if algorithm == "DFS":
            self.dfs_result = {
                "path_length": len(self.path) - 1,
                "visited_cells": len(self.visited),
                "time": execution_time
            }
        else:
            self.bfs_result = {
                "path_length": len(self.path) - 1,
                "visited_cells": len(self.visited),
                "time": execution_time
            }

        self.result_text.delete(1.0, tk.END)  # Clears previous text in the results display.
        self.result_text.insert(tk.END, result_info)  # Inserts the new results.

    def dfs_solve(self):
        start = Node(self.maze_data.entrance[0], self.maze_data.entrance[1])
        end = Node(self.maze_data.exit[0], self.maze_data.exit[1])

        self.visited = set()  # Initializes an empty set to keep track of visited cells.
        self.path = []  # Initializes an empty list to store the final path.
        self.steps = []  # Initializes an empty list to store visualization steps.

        stack = [start]  # Initializes the stack for DFS with the starting node.
        parent_map = {}  # Dictionary to store parent of each node for path reconstruction.

        while stack:
            current = stack.pop()  # Retrieves the last node from the stack (LIFO).

            # Ziyaret edilen bir düğümü tekrar ziyaret etme
            if (current.row, current.col) in self.visited:
                continue

            # Ziyaret edildi olarak işaretle
            self.visited.add((current.row, current.col))

            # Bu adımı adım listesine ekle (ziyaret edilenler ve mevcut yığın)
            step_visited = self.visited.copy()
            step_stack = [node for node in stack]
            self.steps.append({
                "visited": step_visited,
                "frontier": [(node.row, node.col) for node in step_stack],
                "current": (current.row, current.col)
            })

            # Hedefe ulaştık mı?
            if current.row == end.row and current.col == end.col:
                # Yolu geri izle
                self.path = self.backtrack_path(parent_map, start, end)
                return

            # Check neighbors (in order: up, left, down, right)
            neighbors = self.get_neighbors(current)

            # Neighbors are added in reverse order to ensure consistent exploration order (right, down, left, up)
            for neighbor in reversed(neighbors):
                if (neighbor.row, neighbor.col) not in self.visited:
                    stack.append(neighbor)  # Adds unvisited neighbors to the stack.
                    parent_map[(neighbor.row, neighbor.col)] = (current.row, current.col)  # Records the parent of the neighbor.

        # Yol bulunamadı
        messagebox.showinfo("Information", "Target not reached!")

    def bfs_solve(self):
        start = Node(self.maze_data.entrance[0], self.maze_data.entrance[1])
        end = Node(self.maze_data.exit[0], self.maze_data.exit[1])

        self.visited = set()  # Initializes an empty set for visited cells.
        self.path = []  # Initializes an empty list for the final path.
        self.steps = []  # Initializes an empty list for visualization steps.

        queue = deque([start])  # Initializes the queue for BFS with the starting node. `deque` is efficient for popleft().
        parent_map = {}  # Dictionary to store parent of each node for path reconstruction.

        while queue:
            current = queue.popleft()  # Retrieves the first node from the queue (FIFO).

            # Ziyaret edilen bir düğümü tekrar ziyaret etme
            if (current.row, current.col) in self.visited:
                continue

            # Ziyaret edildi olarak işaretle
            self.visited.add((current.row, current.col))  # Marks the current node as visited.

            # Bu adımı adım listesine ekle
            step_visited = self.visited.copy()
            step_queue = [node for node in queue]
            self.steps.append({
                "visited": step_visited,
                "frontier": [(node.row, node.col) for node in step_queue],
                "current": (current.row, current.col)
            })

            # Hedefe ulaştık mı?
            if current.row == end.row and current.col == end.col:
                # Yolu geri izle
                self.path = self.backtrack_path(parent_map, start, end)  # Reconstructs the path.
                return

            # Komşuları kontrol et (sırasıyla: yukarı, sol, aşağı, sağ)
            neighbors = self.get_neighbors(current)

            for neighbor in neighbors:
                if (neighbor.row, neighbor.col) not in self.visited:
                    queue.append(neighbor)
                    if (neighbor.row, neighbor.col) not in parent_map:
                        parent_map[(neighbor.row, neighbor.col)] = (current.row, current.col)

        # Yol bulunamadı
        messagebox.showinfo("Information", "Target not reached!")

    def get_neighbors(self, node):
        neighbors = []
        directions = [
            (-1, 0),  # Up: change in row = -1, change in col = 0
            (0, -1),  # Left: change in row = 0, change in col = -1
            (1, 0),  # Down: change in row = 1, change in col = 0
            (0, 1)  # Right: change in row = 0, change in col = 1
        ]

        for dr, dc in directions:
            new_row = node.row + dr
            new_col = node.col + dc

            # Sınırları kontrol et
            if (0 <= new_row < self.maze_data.rows and
                    0 <= new_col < self.maze_data.cols):

                # Duvar kontrolü
                can_move = True

                # Yukarı hareket
                if dr == -1 and node.row > 0:
                    if self.maze_data.walls_h[node.row][node.col]:
                        can_move = False

                # Aşağı hareket
                elif dr == 1 and node.row < self.maze_data.rows - 1:
                    if self.maze_data.walls_h[node.row + 1][node.col]:
                        can_move = False

                # Sola hareket
                elif dc == -1 and node.col > 0:
                    if self.maze_data.walls_v[node.row][node.col]:
                        can_move = False

                # Sağa hareket
                elif dc == 1 and node.col < self.maze_data.cols - 1:
                    if self.maze_data.walls_v[node.row][node.col + 1]:
                        can_move = False

                # Geçerli komşuyu yeni bir node nesnesi olarak ekle.
                if can_move:
                    neighbors.append(Node(new_row, new_col, node))

        return neighbors

    def backtrack_path(self, parent_map, start, end):
        path = []
        current = (end.row, end.col)  # Starts backtracking from the end node.

        while current != (start.row, start.col):  # Continues until the start node is reached.
            path.append(current)  # Adds the current node to the path.
            current = parent_map[current]  # Moves to the parent of the current node.

        path.append((start.row, start.col))  # Adds the start node to the path.
        path.reverse()  # Reverses the path to get it from start to end.

        return path

    def draw_path(self):
        # Ziyaret edilen hücreleri renklendir.
        for row, col in self.visited:
            x1 = self.start_col + col * self.cell_size
            y1 = self.start_row + row * self.cell_size
            x2 = x1 + self.cell_size
            y2 = y1 + self.cell_size
            self.canvas.create_rectangle(x1, y1, x2, y2, fill="#eee", outline="", tags="visited")

        # Çözüm yolunu çiz.
        if self.path:
            for i in range(len(self.path) - 1):
                row1, col1 = self.path[i]
                row2, col2 = self.path[i + 1]

                x1 = self.start_col + col1 * self.cell_size + self.cell_size // 2
                y1 = self.start_row + row1 * self.cell_size + self.cell_size // 2
                x2 = self.start_col + col2 * self.cell_size + self.cell_size // 2
                y2 = self.start_row + row2 * self.cell_size + self.cell_size // 2

                self.canvas.create_line(x1, y1, x2, y2, fill="red", width=2, arrow=tk.LAST, tags="path")

    # Deletes all elements tagged as "..."
    def clear_solution(self):
        self.canvas.delete("visited")
        self.canvas.delete("path")
        self.canvas.delete("step")
        self.canvas.delete("frontier")
        self.current_step = 0
        self.step_counter_var.set("0/0")
        self.step_control_frame.pack_forget()  # Hides the step-by-step control frame.
        self.steps = []  # Clears the stored visualization steps.
        self.draw_maze()  # Redraws the maze to its initial state.

    def show_steps(self):
        if not self.steps:
            messagebox.showinfo("Information", "Run a maze solution first.")
            return

        # Adım kontrollerini göster
        self.step_control_frame.pack(pady=10)

        # İlk adımı göster
        self.current_step = 0
        self.total_steps = len(self.steps)
        self.step_counter_var.set(f"1/{self.total_steps}")
        self.draw_step(self.current_step)

    def draw_step(self, step_index):
        # Önceki adım gösterimini temizle
        self.canvas.delete("step")
        self.canvas.delete("frontier")

        if step_index < 0 or step_index >= len(self.steps):
            return

        step_data = self.steps[step_index]

        # Ziyaret edilen hücreleri işaretle
        for row, col in step_data["visited"]:
            x1 = self.start_col + col * self.cell_size
            y1 = self.start_row + row * self.cell_size
            x2 = x1 + self.cell_size
            y2 = y1 + self.cell_size
            self.canvas.create_rectangle(x1, y1, x2, y2, fill="#eee", outline="", tags="step")

        # Sınırdaki hücreleri işaretle (frontier)
        for row, col in step_data["frontier"]:
            x1 = self.start_col + col * self.cell_size
            y1 = self.start_row + row * self.cell_size
            x2 = x1 + self.cell_size
            y2 = y1 + self.cell_size
            self.canvas.create_rectangle(x1, y1, x2, y2, fill="#d1e7ff", outline="", tags="frontier")

        # Şu anki hücreyi işaretle
        row, col = step_data["current"]
        x1 = self.start_col + col * self.cell_size
        y1 = self.start_row + row * self.cell_size
        x2 = x1 + self.cell_size
        y2 = y1 + self.cell_size
        self.canvas.create_rectangle(x1, y1, x2, y2, fill="#ffcccb", outline="", tags="step")

    def next_step(self):
        if self.current_step < self.total_steps - 1:  # Checks if there are more steps to show.
            self.current_step += 1  # Increments the current step index.
            self.step_counter_var.set(f"{self.current_step + 1}/{self.total_steps}")  # Updates the step counter display.
            self.draw_step(self.current_step)  # Draws the next step.

    def prev_step(self):
        if self.current_step > 0:  # Checks if it's not the first step.
            self.current_step -= 1  # Decrements the current step index.
            self.step_counter_var.set(f"{self.current_step + 1}/{self.total_steps}")  # Updates the step counter display.
            self.draw_step(self.current_step)  # Draws the previous step.

    def compare_algorithms(self):
        if not hasattr(self.maze_data, 'rows') or self.maze_data.rows == 0:
            messagebox.showinfo("Warning", "First load a maze file.")
            return

        # DFS çözümü
        self.algo_var.set("DFS")
        self.solve_maze()
        dfs_path_length = len(self.path) - 1
        dfs_visited = len(self.visited)
        dfs_time = self.dfs_result["time"]

        # BFS çözümü
        self.algo_var.set("BFS")
        self.solve_maze()
        bfs_path_length = len(self.path) - 1
        bfs_visited = len(self.visited)
        bfs_time = self.bfs_result["time"]

        # Karşılaştırma sonuçlarını göster
        result_info = "*** COMPARE ***\n\n"
        result_info += "DFS:\n"
        result_info += f"- Path Length: {dfs_path_length}\n"
        result_info += f"- Visited: {dfs_visited}\n"
        result_info += f"- Time: {dfs_time:.6f} sn\n\n"

        result_info += "BFS:\n"
        result_info += f"- Path Length: {bfs_path_length}\n"
        result_info += f"- Visited: {bfs_visited}\n"
        result_info += f"- Time: {bfs_time:.6f} sn\n\n"

        if dfs_path_length < bfs_path_length:
            result_info += "DFS found a shorter route."
        elif bfs_path_length < dfs_path_length:
            result_info += "BFS found a shorter route."
        else:
            result_info += "Both algorithms found the same length of path."

        self.result_text.delete(1.0, tk.END)
        self.result_text.insert(tk.END, result_info)

    def create_new_maze(self):
        rows = self.rows_var.get()
        cols = self.cols_var.get()

        data = MazeData()
        data.rows = rows
        data.cols = cols

        # Tüm kenarlarda duvar olan boş bir labirent oluştur
        data.walls_h = [[False] * (cols + 1) for _ in range(rows + 1)]  # Initializes horizontal walls as all false (no walls).
        data.walls_v = [[False] * (cols + 1) for _ in range(rows + 1)]  # Initializes vertical walls as all false (no walls).

        # Dış duvarları ekle
        for i in range(cols + 1):
            data.walls_h[0][i] = True  # Adds top border walls.
            data.walls_h[rows][i] = True  # Adds bottom border walls.
        for i in range(rows + 1):
            data.walls_v[i][0] = True  # Adds left border walls.
            data.walls_v[i][cols] = True  # Adds right border walls.

        # Başlangıç ve bitiş noktalarını ayarla
        data.entrance = (0, 0)  # Sets default entrance to top-left.
        data.exit = (rows - 1, cols - 1)  # Sets default exit to bottom-right.

        self.maze_data = data
        self.draw_maze()

        # Labirent düzenlemeyi etkinleştir
        self.editing = True  # Activates maze editing mode.
        messagebox.showinfo("Information",
                             "The maze was created.\nYou can click to add or remove walls.\nClick on the letters A and B to change the start and end points.")

    def handle_canvas_click(self, event):
        if not self.editing or not hasattr(self.maze_data, 'rows') or self.maze_data.rows == 0:
            return

        # Tıklanan konumu bul
        x, y = event.x, event.y

        cell_row = (y - self.start_row) // self.cell_size  # Calculates the row of the clicked cell.
        cell_col = (x - self.start_col) // self.cell_size  # Calculates the column of the clicked cell.

        if cell_row < 0 or cell_row > self.maze_data.rows or cell_col < 0 or cell_col > self.maze_data.cols:
            return

        y_in_cell = (y - self.start_row) % self.cell_size  # Relative Y coordinate within the clicked cell.
        x_in_cell = (x - self.start_col) % self.cell_size  # Relative X coordinate within the clicked cell.

        if y_in_cell < 5 and cell_row >= 0 and cell_row <= self.maze_data.rows:  # Checks if click is near a horizontal wall boundary.
            if 0 <= cell_col < self.maze_data.cols:
                self.maze_data.walls_h[cell_row][cell_col] = not self.maze_data.walls_h[cell_row][cell_col]  # Toggles the horizontal wall.
                self.draw_maze()
        elif x_in_cell < 5 and cell_col >= 0 and cell_col <= self.maze_data.cols:  # Checks if click is near a vertical wall boundary.
            if 0 <= cell_row < self.maze_data.rows:
                self.maze_data.walls_v[cell_row][cell_col] = not self.maze_data.walls_v[cell_row][cell_col]  # Toggles the vertical wall.
                self.draw_maze()

        # Hücrenin ortasına tıklandı mı? (Başlangıç/bitiş noktası değiştirmek için)
        elif 5 <= y_in_cell <= self.cell_size - 5 and 5 <= x_in_cell <= self.cell_size - 5:
            # Başlangıç ve bitiş noktasını değiştir (merkeze daha yakın tıklandığında)
            if abs(y_in_cell - self.cell_size // 2) < 10 and abs(x_in_cell - self.cell_size // 2) < 10:
                if (cell_row, cell_col) == self.maze_data.entrance:
                    # Yeni bir başlangıç noktası seçmek için pencere göster
                    self.select_entrance_exit("entrance")
                elif (cell_row, cell_col) == self.maze_data.exit:
                    # Yeni bir bitiş noktası seçmek için pencere göster
                    self.select_entrance_exit("exit")
                else:
                    # Başlangıç veya bitiş noktası olarak değiştir
                    selection = messagebox.askyesnocancel("Change Point",
                                                          "What would you like to change this point to?\n"
                                                          "Yes: Start (A)\n"
                                                          "No: Finish (B)\n"
                                                          "Cancel: Do Not Change")
                    if selection is True:  # Yes ise giriş
                        self.maze_data.entrance = (cell_row, cell_col)
                        self.draw_maze()
                    elif selection is False:  # No ise çıkış
                        self.maze_data.exit = (cell_row, cell_col)
                        self.draw_maze()

    def select_entrance_exit(self, point_type):
        # Yeni bir başlangıç veya bitiş noktası seçmek için pencere
        window = tk.Toplevel(self.root)
        window.title(f"Select Point: {'Start' if point_type == 'entrance' else 'Finish'}")
        window.geometry("300x180")
        window.resizable(False, False)

        # Satır ve sütun seçimi için visualization
        frame = tk.Frame(window, padx=10, pady=10)
        frame.pack(fill=tk.BOTH, expand=True)

        tk.Label(frame, text="Line:").grid(row=0, column=0, sticky=tk.W, pady=5)
        row_var = tk.IntVar(value=0)
        row_spin = tk.Spinbox(frame, from_=0, to=self.maze_data.rows - 1, textvariable=row_var, width=5)
        row_spin.grid(row=0, column=1, sticky=tk.W, pady=5)

        tk.Label(frame, text="Column:").grid(row=1, column=0, sticky=tk.W, pady=5)
        col_var = tk.IntVar(value=0)
        col_spin = tk.Spinbox(frame, from_=0, to=self.maze_data.cols - 1, textvariable=col_var, width=5)
        col_spin.grid(row=1, column=1, sticky=tk.W, pady=5)

        def set_point():
            row = row_var.get()
            col = col_var.get()

            if point_type == "entrance":
                self.maze_data.entrance = (row, col)  # Updates the entrance coordinates.
            else:
                self.maze_data.exit = (row, col)  # Updates the exit coordinates.

            self.draw_maze()
            window.destroy()  # Closes the selection window.

        tk.Button(frame, text="Okey", command=set_point).grid(row=2, column=0, columnspan=2, pady=10)

    def save_maze(self):
        if not hasattr(self.maze_data, 'rows') or self.maze_data.rows == 0:
            messagebox.showinfo("Warning", "No maze found to save.")
            return

        file_path = filedialog.asksaveasfilename(
            defaultextension=".txt",
            filetypes=[("Text files", "*.txt"), ("All files", "*.*")]
        )

        if file_path:
            try:
                with open(file_path, 'w') as file:
                    for r in range(self.maze_data.rows + 1):
                        line = ""
                        for c in range(self.maze_data.cols + 1):
                            # Checks if a vertical wall exists at the current position.
                            if c < len(self.maze_data.walls_v[0]) and r < len(self.maze_data.walls_v) and \
                                    self.maze_data.walls_v[r][c]:
                                line += "| "  # Adds '|' for a vertical wall.
                            else:
                                line += "  "  # Adds spaces if no vertical wall.
                        file.write(line.rstrip() + "\n")  # Writes the line to the file, removing trailing spaces.

                        if r < self.maze_data.rows:
                            line = ""
                            for c in range(self.maze_data.cols + 1):
                                # Checks if a horizontal wall exists at the current position.
                                if c < len(self.maze_data.walls_h[0]) and r < len(self.maze_data.walls_h) and \
                                        self.maze_data.walls_h[r + 1][c]:
                                    line += "- "  # Adds '-' for a horizontal wall.
                                else:
                                    line += "  "  # Adds spaces if no horizontal wall.
                            file.write(line.rstrip() + "\n")

                    # Giriş ve çıkış noktaları
                    file.write(f"{self.maze_data.entrance[0]},{self.maze_data.entrance[1]}\n")
                    file.write(f"{self.maze_data.exit[0]},{self.maze_data.exit[1]}\n")

                messagebox.showinfo("Successful", "Maze saved successfully.")
            except Exception as e:
                messagebox.showerror("Error", f"An error occurred while saving the file:\n{str(e)}")

if __name__ == "__main__":
    root = tk.Tk()
    app = MazeSolver(root)
    root.mainloop()