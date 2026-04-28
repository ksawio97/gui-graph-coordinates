import matplotlib.pyplot as plt

# load coordinates
coords = {}
with open("out.txt") as f:
    for line in f:
        parts = line.split()
        if len(parts) == 3:
            v, x, y = int(parts[0]), float(parts[1]), float(parts[2])
            coords[v] = (x, y)

fig, ax = plt.subplots()

# draw edges
with open("inp.txt") as f:
    for line in f:
        parts = line.split()
        if len(parts) == 4:
            a, b = int(parts[1]), int(parts[2])
            x0, y0 = coords[a]
            x1, y1 = coords[b]
            ax.plot([x0, x1], [y0, y1], 'k-', linewidth=0.8, zorder=1)

# draw vertices and labels
for v, (x, y) in coords.items():
    ax.plot(x, y, 'o', color='steelblue', markersize=8, zorder=2)
    ax.annotate(f"{v} ({x:.2f}, {y:.2f})", (x, y),
                textcoords="offset points", xytext=(6, 4), fontsize=7)

ax.set_aspect('equal')
ax.set_title("Graph Layout")
plt.tight_layout()
plt.savefig("graph.png", dpi=240)
plt.show()
