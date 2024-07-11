CREATE OR REPLACE FUNCTION video_like_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        IF NEW.status = 'LIKE' THEN
UPDATE video SET like_count = COALESCE(like_count, 0) + 1 WHERE id = NEW.video_id;
ELSIF NEW.status = 'DISLIKE' THEN
UPDATE video SET dislike_count = COALESCE(dislike_count, 0) + 1 WHERE id = NEW.video_id;
END IF;
    ELSIF TG_OP = 'UPDATE' THEN
        IF OLD.status = 'LIKE' AND NEW.status = 'DISLIKE' THEN
UPDATE video SET like_count = COALESCE(like_count, 0) - 1, dislike_count = COALESCE(dislike_count, 0) + 1 WHERE id = NEW.video_id;
ELSIF OLD.status = 'DISLIKE' AND NEW.status = 'LIKE' THEN
UPDATE video SET like_count = COALESCE(like_count, 0) + 1, dislike_count = COALESCE(dislike_count, 0) - 1 WHERE id = NEW.video_id;
END IF;
    ELSIF TG_OP = 'DELETE' THEN
        IF OLD.status = 'LIKE' THEN
UPDATE video SET like_count = COALESCE(like_count, 0) - 1 WHERE id = OLD.video_id;
ELSIF OLD.status = 'DISLIKE' THEN
UPDATE video SET dislike_count = COALESCE(dislike_count, 0) - 1 WHERE id = OLD.video_id;
END IF;
RETURN OLD;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER video_like_trigger
    BEFORE INSERT OR UPDATE OR DELETE
                     ON video_like
                         FOR EACH ROW
                         EXECUTE FUNCTION video_like_function();
