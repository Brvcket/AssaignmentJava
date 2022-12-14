import java.util.Map;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;

/**
 * Main class of the program.
 */
public final class Main {

    /**
     * variable for Board class.
     *
     * @see Board
     */
    private static Board chessBoard;
    private Main() {
    }

    /**
     * Main method.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) throws IOException {
        InvalidBoardSizeException invalidBoardSizeException = new InvalidBoardSizeException();
        InvalidNumberOfPiecesException invalidNumberOfPiecesException = new InvalidNumberOfPiecesException();
        InvalidPieceNameException invalidPieceNameException = new InvalidPieceNameException();
        InvalidPieceColorException invalidPieceColorException = new InvalidPieceColorException();
        InvalidPiecePositionException invalidPiecePositionException = new InvalidPiecePositionException();
        InvalidGivenKingsException invalidGivenKingsException = new InvalidGivenKingsException();
        InvalidInputException invalidInputException = new InvalidInputException();
        Scanner input = new Scanner(new FileInputStream("input.txt"));
        FileOutputStream output = new FileOutputStream("output.txt");
        try {
            int n = input.nextInt();
            final int minBoardSize = 3;
            final int maxBoardSize = 1000;
            if (n < minBoardSize || n > maxBoardSize) {
                output.write(invalidBoardSizeException.getMessage().getBytes());
                System.exit(0);
            }
            int m = input.nextInt();
            final int minPiecesCount = 2;
            if (m < minPiecesCount || m > n * n) {
                output.write(invalidNumberOfPiecesException.getMessage().getBytes());
                System.exit(0);
            }
            chessBoard = new Board(n);
            int countOfBlackKings = 0;
            int countOfWhiteKings = 0;
            ChessPiece[] pieces = new ChessPiece[m];
            for (int i = 0; i < m; i++) {
                String pieceType = input.next();
                String color = input.next();
                ChessPiece piece = null;
                if (PieceColor.parse(color) == null) {
                    output.write(invalidPieceColorException.getMessage().getBytes());
                    System.exit(0);
                }
                int x = input.nextInt();
                int y = input.nextInt();
                PiecePosition piecePosition = new PiecePosition(x, y);
                switch (pieceType) {
                    case "Knight":
                        piece = new Knight(piecePosition, PieceColor.parse(color));
                        break;
                    case "King":
                        piece = new King(piecePosition, PieceColor.parse(color));
                        if (PieceColor.parse(color) == PieceColor.WHITE) {
                            countOfWhiteKings++;
                        } else {
                            countOfBlackKings++;
                        }
                        break;
                    case "Pawn":
                        piece = new Pawn(piecePosition, PieceColor.parse(color));
                        break;
                    case "Bishop":
                        piece = new Bishop(piecePosition, PieceColor.parse(color));
                        break;
                    case "Rook":
                        piece = new Rook(piecePosition, PieceColor.parse(color));
                        break;
                    case "Queen":
                        piece = new Queen(piecePosition, PieceColor.parse(color));
                        break;
                    default:
                        output.write(invalidPieceNameException.getMessage().getBytes());
                        System.exit(0);
                }
                pieces[i] = piece;
                if (x < 1 || x > n || y < 1 || y > n) {
                    output.write(invalidPiecePositionException.getMessage().getBytes());
                    System.exit(0);
                }
                if (chessBoard.getPiece(piecePosition) == null) {
                    chessBoard.addPiece(piece);
                } else {
                    output.write(invalidPiecePositionException.getMessage().getBytes());
                    System.exit(0);
                }
            }
            if (input.hasNext()) {
                output.write(invalidNumberOfPiecesException.getMessage().getBytes());
                System.exit(0);
            }
            if (countOfWhiteKings != 1 || countOfBlackKings != 1) {
                output.write(invalidGivenKingsException.getMessage().getBytes());
                System.exit(0);
            }
            for (ChessPiece piece : pieces) {
                output.write((chessBoard.getPiecePossibleMovesCount(piece) + " "
                        + chessBoard.getPiecePossibleCapturesCount(piece) + "\n").getBytes());
            }
        } catch (java.lang.Exception e) {
            output.write(invalidInputException.getMessage().getBytes());
            System.exit(0);
        }
    }

}

/**
 * enumeration of colors of the pieces.
 */
enum PieceColor {
    /**
     * The color of the white piece.
     */
    WHITE,
    /**
     * The color of the black piece.
     */
    BLACK;

    /**
     * Method for parsing the color of the piece.
     *
     * @param color the color of the piece
     * @return the color of the piece
     */
    public static PieceColor parse(String color) {
        if (color.equals("White")) {
            return WHITE;
        } else if (color.equals("Black")) {
            return BLACK;
        } else {
            return null;
        }
    }
}

/**
 * Interface for diagonal movement.
 *
 * @see Bishop
 * @see Queen
 */
interface BishopMovement {
    /**
     * Method for counting the possible moves of the bishop.
     *
     * @param position  the position of the piece
     * @param color     the color of the piece
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible diagonal moves
     */
    default int getDiagonalMovesCount(PiecePosition position, PieceColor color,
                                      Map<String, ChessPiece> positions, int boardSize) {
        int movesCount = 0;
        int x = position.getX();
        int y = position.getY();
        while (x < boardSize && y < boardSize) {
            x++;
            y++;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    movesCount++;
                }
                break;
            } else {
                movesCount++;
            }
        }
        x = position.getX();
        y = position.getY();
        while (x < boardSize && y > 1) {
            x++;
            y--;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    movesCount++;
                }
                break;
            } else {
                movesCount++;
            }
        }
        x = position.getX();
        y = position.getY();
        while (x > 1 && y < boardSize) {
            x--;
            y++;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    movesCount++;
                }
                break;
            } else {
                movesCount++;
            }
        }
        x = position.getX();
        y = position.getY();
        while (x > 1 && y > 1) {
            x--;
            y--;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    movesCount++;
                }
                break;
            } else {
                movesCount++;
            }
        }
        return movesCount;
    }

    /**
     * Method for counting the possible captures of the bishop.
     *
     * @param position  the position of the piece
     * @param color     the color of the piece
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible diagonal captures
     */
    default int getDiagonalCapturesCount(PiecePosition position, PieceColor color,
                                         Map<String, ChessPiece> positions, int boardSize) {
        int capturesCount = 0;
        int x = position.getX();
        int y = position.getY();
        while (x < boardSize && y < boardSize) {
            x++;
            y++;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    capturesCount++;
                }
                break;
            }
        }
        x = position.getX();
        y = position.getY();
        while (x < boardSize && y >= 0) {
            x++;
            y--;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    capturesCount++;
                }
                break;
            }
        }
        x = position.getX();
        y = position.getY();
        while (x >= 0 && y < boardSize) {
            x--;
            y++;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    capturesCount++;
                }
                break;
            }
        }
        x = position.getX();
        y = position.getY();
        while (x >= 0 && y >= 0) {
            x--;
            y--;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    capturesCount++;
                }
                break;
            }
        }
        return capturesCount;
    }
}

/**
 * Interface for horizontal and vertical movement.
 *
 * @see Rook
 * @see Queen
 */
interface RookMovement {
    /**
     * Method for counting the possible moves of the rook.
     *
     * @param position  the position of the piece
     * @param color     the color of the piece
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible horizontal and vertical moves
     */
    default int getOrthogonalMovesCount(PiecePosition position, PieceColor color,
                                        Map<String, ChessPiece> positions, int boardSize) {
        int movesCount = 0;
        int x = position.getX();
        int y = position.getY();
        while (x < boardSize) {
            x++;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    movesCount++;
                }
                break;
            } else {
                movesCount++;
            }
        }
        x = position.getX();
        y = position.getY();
        while (x > 1) {
            x--;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    movesCount++;
                }
                break;
            } else {
                movesCount++;
            }
        }
        x = position.getX();
        y = position.getY();
        while (y < boardSize) {
            y++;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    movesCount++;
                }
                break;
            } else {
                movesCount++;
            }
        }
        x = position.getX();
        y = position.getY();
        while (y > 1) {
            y--;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    movesCount++;
                }
                break;
            } else {
                movesCount++;
            }
        }
        return movesCount;
    }

    /**
     * Method for counting the possible captures of the rook.
     *
     * @param position  the position of the piece
     * @param color     the color of the piece
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible horizontal and vertical captures
     */
    default int getOrthogonalCapturesCount(PiecePosition position, PieceColor color,
                                           Map<String, ChessPiece> positions, int boardSize) {
        int capturesCount = 0;
        int x = position.getX();
        int y = position.getY();
        while (x < boardSize) {
            x++;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    capturesCount++;
                }
                break;
            }
        }
        x = position.getX();
        y = position.getY();
        while (x >= 0) {
            x--;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    capturesCount++;
                }
                break;
            }
        }
        x = position.getX();
        y = position.getY();
        while (y < boardSize) {
            y++;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    capturesCount++;
                }
                break;
            }
        }
        x = position.getX();
        y = position.getY();
        while (y >= 0) {
            y--;
            if (positions.containsKey(x + " " + y)) {
                if (positions.get(x + " " + y).getColor() != color) {
                    capturesCount++;
                }
                break;
            }
        }
        return capturesCount;
    }
}



/**
 * Class for work with the board.
 */
class Board {
    /**
     * map of position to pieces.
     */
    private final Map<String, ChessPiece> positionsToPieces = new HashMap<>();
    /**
     * size of the board.
     */
    private final int size;

    /**
     * Constructor for the board.
     *
     * @param boardSize the size of the board
     */
    Board(int boardSize) {
        size = boardSize;
    }

    /**
     * Method for getting possible moves count for the piece.
     *
     * @param piece the piece
     * @return the number of possible moves
     */
    public int getPiecePossibleMovesCount(ChessPiece piece) {
        return piece.getMovesCount(positionsToPieces, size);
    }

    /**
     * Method for getting possible captures count for the piece.
     *
     * @param piece the piece
     * @return the number of possible captures
     */
    public int getPiecePossibleCapturesCount(ChessPiece piece) {
        return piece.getCapturesCount(positionsToPieces, size);
    }

    /**
     * Method for adding a piece to the board.
     *
     * @param piece the piece
     */
    public void addPiece(ChessPiece piece) {
        positionsToPieces.put(piece.getPosition().toString(), piece);
    }

    /**
     * Method for getting a piece by its position.
     *
     * @param position the position of the piece
     * @return the piece
     */
    public ChessPiece getPiece(PiecePosition position) {
        return positionsToPieces.get(position.toString());
    }
}

/**
 * Class for work with the position of the piece.
 */
class PiecePosition {
    /**
     * x coordinate.
     */
    private final int x;
    /**
     * y coordinate.
     */
    private final int y;

    /**
     * Constructor for the position.
     *
     * @param onX the x coordinate
     * @param onY the y coordinate
     */
    PiecePosition(int onX, int onY) {
        x = onX;
        y = onY;
    }

    /**
     * Method for getting the x coordinate.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Method for getting the y coordinate.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Method for getting the string representation of the position.
     *
     * @return the string representation of the position
     */
    public String toString() {
        return x + " " + y;
    }
}

/**
 * Class for work with the piece.
 */
abstract class ChessPiece {
    /**
     * position of the piece.
     */
    protected PiecePosition position;
    /**
     * color of the piece.
     */
    protected PieceColor color;

    /**
     * Constructor for the piece.
     *
     * @param piecePosition the position of the piece
     * @param pieceColor    the color of the piece
     */
    ChessPiece(PiecePosition piecePosition, PieceColor pieceColor) {
        this.position = piecePosition;
        this.color = pieceColor;
    }

    /**
     * Method for getting the position of the piece.
     *
     * @return the position of the piece
     */
    public PiecePosition getPosition() {
        return position;
    }

    /**
     * Method for getting the color of the piece.
     *
     * @return the color of the piece
     */
    public PieceColor getColor() {
        return color;
    }

    /**
     * Method for getting the possible moves count of the piece.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible moves
     */

    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        return 0;
    }

    /**
     * Method for getting the possible captures count of the piece.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible captures
     */
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        return 0;
    }
}

/**
 * Class for the knight chess piece.
 */
class Knight extends ChessPiece {
    /**
     * Constructor for the knight.
     *
     * @param position the position of the knight
     * @param color    the color of the knight
     */
    Knight(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    /**
     * Method for getting the possible moves count of the knight.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible moves
     */
    @Override
    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        int movesCount = 0;
        int x = position.getX();
        int y = position.getY();
        if (x + 2 <= boardSize && y + 1 <= boardSize) {
            if (!positions.containsKey((x + 2) + " " + (y + 1))) {
                movesCount++;
            } else if (positions.get((x + 2) + " " + (y + 1)).getColor() != color) {
                movesCount++;
            }
        }
        if (x + 2 <= boardSize && y - 1 > 0) {
            if (!positions.containsKey((x + 2) + " " + (y - 1))) {
                movesCount++;
            } else if (positions.get((x + 2) + " " + (y - 1)).getColor() != color) {
                movesCount++;
            }
        }
        if (x - 2 > 0 && y + 1 <= boardSize) {
            if (!positions.containsKey((x - 2) + " " + (y + 1))) {
                movesCount++;
            } else if (positions.get((x - 2) + " " + (y + 1)).getColor() != color) {
                movesCount++;
            }
        }
        if (x - 2 > 0 && y - 1 > 0) {
            if (!positions.containsKey((x - 2) + " " + (y - 1))) {
                movesCount++;
            } else if (positions.get((x - 2) + " " + (y - 1)).getColor() != color) {
                movesCount++;
            }
        }
        if (x + 1 <= boardSize && y + 2 <= boardSize) {
            if (!positions.containsKey((x + 1) + " " + (y + 2))) {
                movesCount++;
            } else if (positions.get((x + 1) + " " + (y + 2)).getColor() != color) {
                movesCount++;
            }
        }
        if (x + 1 <= boardSize && y - 2 > 0) {
            if (!positions.containsKey((x + 1) + " " + (y - 2))) {
                movesCount++;
            } else if (positions.get((x + 1) + " " + (y - 2)).getColor() != color) {
                movesCount++;
            }
        }
        if (x - 1 > 0 && y + 2 <= boardSize) {
            if (!positions.containsKey((x - 1) + " " + (y + 2))) {
                movesCount++;
            } else if (positions.get((x - 1) + " " + (y + 2)).getColor() != color) {
                movesCount++;
            }
        }
        if (x - 1 > 0 && y - 2 > 0) {
            if (!positions.containsKey((x - 1) + " " + (y - 2))) {
                movesCount++;
            } else if (positions.get((x - 1) + " " + (y - 2)).getColor() != color) {
                movesCount++;
            }
        }
        return movesCount;
    }

    /**
     * Method for getting the possible captures count of the knight.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible captures
     */
    @Override
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        int capturesCount = 0;
        int x = position.getX();
        int y = position.getY();
        if (x + 2 <= boardSize && y + 1 <= boardSize) {
            if (positions.containsKey((x + 2) + " " + (y + 1))) {
                if (positions.get((x + 2) + " " + (y + 1)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (x + 2 <= boardSize && y - 1 > 0) {
            if (positions.containsKey((x + 2) + " " + (y - 1))) {
                if (positions.get((x + 2) + " " + (y - 1)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (x - 2 > 0 && y + 1 <= boardSize) {
            if (positions.containsKey((x - 2) + " " + (y + 1))) {
                if (positions.get((x - 2) + " " + (y + 1)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (x - 2 > 0 && y - 1 > 0) {
            if (positions.containsKey((x - 2) + " " + (y - 1))) {
                if (positions.get((x - 2) + " " + (y - 1)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (x + 1 <= boardSize && y + 2 <= boardSize) {
            if (positions.containsKey((x + 1) + " " + (y + 2))) {
                if (positions.get((x + 1) + " " + (y + 2)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (x + 1 <= boardSize && y - 2 > 0) {
            if (positions.containsKey((x + 1) + " " + (y - 2))) {
                if (positions.get((x + 1) + " " + (y - 2)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (x - 1 > 0 && y + 2 <= boardSize) {
            if (positions.containsKey((x - 1) + " " + (y + 2))) {
                if (positions.get((x - 1) + " " + (y + 2)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (x - 1 > 0 && y - 2 > 0) {
            if (positions.containsKey((x - 1) + " " + (y - 2))) {
                if (positions.get((x - 1) + " " + (y - 2)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        return capturesCount;
    }
}

/**
 * Class for king chess piece.
 */
class King extends ChessPiece {
    /**
     * Constructor for the king.
     *
     * @param position the position of the king
     * @param color    the color of the king
     */
    King(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    /**
     * Method for getting the possible moves count of the king.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible moves
     */
    @Override
    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        int movesCount = 0;
        int x = position.getX();
        int y = position.getY();
        if (x + 1 <= boardSize) {
            if (!positions.containsKey((x + 1) + " " + y)) {
                movesCount++;
            } else if (positions.get((x + 1) + " " + y).getColor() != color) {
                movesCount++;
            }
        }
        if (x - 1 > 0) {
            if (!positions.containsKey((x - 1) + " " + y)) {
                movesCount++;
            } else if (positions.get((x - 1) + " " + y).getColor() != color) {
                movesCount++;
            }
        }
        if (y + 1 <= boardSize) {
            if (!positions.containsKey(x + " " + (y + 1))) {
                movesCount++;
            } else if (positions.get(x + " " + (y + 1)).getColor() != color) {
                movesCount++;
            }
        }
        if (y - 1 > 0) {
            if (!positions.containsKey(x + " " + (y - 1))) {
                movesCount++;
            } else if (positions.get(x + " " + (y - 1)).getColor() != color) {
                movesCount++;
            }
        }
        if (x + 1 <= boardSize && y + 1 <= boardSize) {
            if (!positions.containsKey((x + 1) + " " + (y + 1))) {
                movesCount++;
            } else if (positions.get((x + 1) + " " + (y + 1)).getColor() != color) {
                movesCount++;
            }
        }
        if (x + 1 <= boardSize && y - 1 > 0) {
            if (!positions.containsKey((x + 1) + " " + (y - 1))) {
                movesCount++;
            } else if (positions.get((x + 1) + " " + (y - 1)).getColor() != color) {
                movesCount++;
            }
        }
        if (x - 1 > 0 && y + 1 <= boardSize) {
            if (!positions.containsKey((x - 1) + " " + (y + 1))) {
                movesCount++;
            } else if (positions.get((x - 1) + " " + (y + 1)).getColor() != color) {
                movesCount++;
            }
        }
        if (x - 1 > 0 && y - 1 > 0) {
            if (!positions.containsKey((x - 1) + " " + (y - 1))) {
                movesCount++;
            } else if (positions.get((x - 1) + " " + (y - 1)).getColor() != color) {
                movesCount++;
            }
        }
        return movesCount;
    }

    /**
     * Method for getting the possible captures count of the king.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible captures
     */
    @Override
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        int capturesCount = 0;
        int x = position.getX();
        int y = position.getY();
        if (x + 1 <= boardSize) {
            if (positions.containsKey((x + 1) + " " + y)) {
                if (positions.get((x + 1) + " " + y).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (x - 1 > 0) {
            if (positions.containsKey((x - 1) + " " + y)) {
                if (positions.get((x - 1) + " " + y).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (y + 1 <= boardSize) {
            if (positions.containsKey(x + " " + (y + 1))) {
                if (positions.get(x + " " + (y + 1)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (y - 1 > 0) {
            if (positions.containsKey(x + " " + (y - 1))) {
                if (positions.get(x + " " + (y - 1)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (x + 1 <= boardSize && y + 1 <= boardSize) {
            if (positions.containsKey((x + 1) + " " + (y + 1))) {
                if (positions.get((x + 1) + " " + (y + 1)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (x + 1 <= boardSize && y - 1 > 0) {
            if (positions.containsKey((x + 1) + " " + (y - 1))) {
                if (positions.get((x + 1) + " " + (y - 1)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (x - 1 > 0 && y + 1 <= boardSize) {
            if (positions.containsKey((x - 1) + " " + (y + 1))) {
                if (positions.get((x - 1) + " " + (y + 1)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        if (x - 1 > 0 && y - 1 > 0) {
            if (positions.containsKey((x - 1) + " " + (y - 1))) {
                if (positions.get((x - 1) + " " + (y - 1)).getColor() != color) {
                    capturesCount++;
                }
            }
        }
        return capturesCount;
    }
}

/**
 * Class for the pawn chess piece.
 */
class Pawn extends ChessPiece {
    /**
     * Constructor for the pawn chess piece.
     *
     * @param color    the color of the pawn
     * @param position the position of the pawn
     */
    Pawn(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    /**
     * Method for getting the possible moves count of the pawn.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible moves
     */
    @Override
    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        int movesCount = 0;
        int x = position.getX();
        int y = position.getY();
        if (color == PieceColor.WHITE) {
            y++;
        } else {
            y--;
        }
        if (x > 0 && x <= boardSize && y > 0 && y <= boardSize) {
            if (positions.get(x + " " + y) == null) {
                movesCount++;
            }
        }
        if (positions.get(x + 1 + " " + y) != null && positions.get(x + 1 + " " + y).getColor() != getColor()) {
            movesCount++;
        }
        if (positions.get(x - 1 + " " + y) != null && positions.get(x - 1 + " " + y).getColor() != getColor()) {
            movesCount++;
        }
        return movesCount;
    }

    /**
     * Method for getting the possible captures count of the pawn.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible captures
     */
    @Override
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        int capturesCount = 0;
        int x = position.getX();
        int y = position.getY();
        if (color == PieceColor.WHITE) {
            y++;
        } else {
            y--;
        }
        if (positions.get(x + 1 + " " + y) != null && positions.get(x + 1 + " " + y).getColor() != getColor()) {
            capturesCount++;
        }
        if (positions.get(x - 1 + " " + y) != null && positions.get(x - 1 + " " + y).getColor() != getColor()) {
            capturesCount++;
        }
        return capturesCount;
    }
}

/**
 * Class for the bishop chess piece.
 */
class Bishop extends ChessPiece implements BishopMovement {
    /**
     * Constructor for the bishop chess piece.
     *
     * @param color    the color of the rook
     * @param position the position of the rook
     */
    Bishop(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    /**
     * Method for getting the possible moves count of the bishop.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible moves
     */
    @Override
    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        return getDiagonalMovesCount(position, color, positions, boardSize);
    }

    /**
     * Method for getting the possible captures count of the bishop.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible captures
     */
    @Override
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        return getDiagonalCapturesCount(position, color, positions, boardSize);
    }
}

/**
 * Class for the rook chess piece.
 */
class Rook extends ChessPiece implements RookMovement {
    /**
     * Constructor for the rook chess piece.
     *
     * @param color    the color of the rook
     * @param position the position of the rook
     */
    Rook(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    /**
     * Method for getting the possible moves count of the rook.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible moves
     */
    @Override
    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        return getOrthogonalMovesCount(position, color, positions, boardSize);
    }

    /**
     * Method for getting the possible captures count of the rook.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible captures
     */
    @Override
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        return getOrthogonalCapturesCount(position, color, positions, boardSize);
    }
}

/**
 * Class for the queen chess piece.
 */
class Queen extends ChessPiece implements BishopMovement, RookMovement {
    /**
     * Constructor for the queen chess piece.
     *
     * @param color    the color of the queen
     * @param position the position of the queen
     */
    Queen(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    /**
     * Method for getting the possible moves count of the queen.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible moves
     */
    @Override
    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        return getDiagonalMovesCount(position, color, positions, boardSize)
                + getOrthogonalMovesCount(position, color, positions, boardSize);
    }

    /**
     * Method for getting the possible captures count of the queen.
     *
     * @param positions the map of the positions
     * @param boardSize the size of the board
     * @return the number of possible captures
     */
    @Override
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        return getDiagonalCapturesCount(position, color, positions, boardSize)
                + getOrthogonalCapturesCount(position, color, positions, boardSize);
    }

}

/**
 * Class for the exceptions.
 */
class Exception {
    /**
     * Method for throwing an exception.
     *
     * @return the message of the exception
     */
    public String getMessage() {
        return "";
    }
}

/**
 * Class for the invalid board size exception.
 */
class InvalidBoardSizeException extends Exception {
    /**
     * Method for throwing an invalid board size exception.
     *
     * @return the message "Invalid board size"
     */
    @Override
    public String getMessage() {
        return "Invalid board size";
    }
}

/**
 * Class for the invalid number of pieces exception.
 */
class InvalidNumberOfPiecesException extends Exception {
    /**
     * Method for throwing an invalid number of pieces exception.
     *
     * @return the message "Invalid number of pieces"
     */
    @Override
    public String getMessage() {
        return "Invalid number of pieces";
    }
}

/**
 * Class for the invalid piece name exception.
 */
class InvalidPieceNameException extends Exception {
    /**
     * Method for throwing an invalid piece name exception.
     *
     * @return the message "Invalid piece name"
     */
    @Override
    public String getMessage() {
        return "Invalid piece name";
    }
}

/**
 * Class for the invalid piece color exception.
 */
class InvalidPieceColorException extends Exception {
    /**
     * Method for throwing an invalid piece color exception.
     *
     * @return the message "Invalid piece color"
     */
    @Override
    public String getMessage() {
        return "Invalid piece color";
    }
}

/**
 * Class for the invalid piece position exception.
 */
class InvalidPiecePositionException extends Exception {
    /**
     * Method for throwing an invalid piece position exception.
     *
     * @return the message "Invalid piece position"
     */
    @Override
    public String getMessage() {
        return "Invalid piece position";
    }
}

/**
 * Class for the invalid given kings exception.
 */
class InvalidGivenKingsException extends Exception {
    /**
     * Method for throwing an invalid given kings exception.
     *
     * @return the message "Invalid given kings"
     */
    @Override
    public String getMessage() {
        return "Invalid given Kings";
    }
}

/**
 * Class for the invalid input position exception.
 */
class InvalidInputException extends Exception {
    /**
     * Method for throwing an invalid input position exception.
     *
     * @return the message "Invalid input"
     */
    @Override
    public String getMessage() {
        return "Invalid input";
    }
}
