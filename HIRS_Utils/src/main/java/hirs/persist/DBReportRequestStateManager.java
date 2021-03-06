package hirs.persist;

import hirs.data.persist.Device;
import hirs.data.persist.ReportRequestState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * This class defines a <code>ReportRequestStateManager</code> that stores ReportRequestStates in a
 * database.
 */
public class DBReportRequestStateManager extends DBManager<ReportRequestState>
        implements ReportRequestStateManager {
    private static final Logger LOGGER = LogManager.getLogger(DBReportRequestStateManager.class);

    /**
     * Creates a new <code>DBReportRequestStateManager</code> that uses the default database. The
     * default database is used to store all of the <code>ReportRequestState</code>s.
     *
     * @param sessionFactory session factory used to access database connections
     */
    public DBReportRequestStateManager(final SessionFactory sessionFactory) {
        super(ReportRequestState.class, sessionFactory);
    }

    /**
     * Retrieves the state of a device, if the device and state exists.
     *
     * @param device the Device whose state should be retrieved
     * @return the associated ReportRequestState, or null if no associated state was found
     */
    @Override
    public final ReportRequestState getState(final Device device) {
        Criterion crit = Restrictions.eq("device", device);
        List<ReportRequestState> results = getWithCriteria(Collections.singletonList(crit));
        if (results.isEmpty()) {
            return null;
        } else {
            LOGGER.debug("Retrieved ReportRequestState: {}", results.get(0));
            return results.get(0);
        }
    }

    /**
     * Return a Collection of all persisted ReportRequestStates in the database.
     *
     * @return the Collection of all persisted ReportRequestStates
     */
    @Override
    public final List<ReportRequestState> getLateDeviceStates() {
        Criterion criterion = Restrictions.le("dueDate", new Date());
        return getWithCriteria(Collections.singletonList(criterion));
    }

    /**
     * Saves the given state to the database. The associated Device must be saved prior to saving
     * the state.
     *
     * @param state the state to save
     * @return the saved copy of the state
     */
    @Override
    public final ReportRequestState saveState(final ReportRequestState state) {
        if (state.getId() == null) {
            return save(state);
        } else {
            update(state);
            ReportRequestState updatedState = getState(state.getDevice());
            LOGGER.debug("Updated ReportRequestState: {}", updatedState);
            return updatedState;
        }
    }

    /**
     * Deletes the given {@link ReportRequestState} from the database.
     *
     * @param state the ReportRequestState instance to delete
     */
    @Override
    public final void deleteState(final ReportRequestState state) {
        delete(state);
    }
}
