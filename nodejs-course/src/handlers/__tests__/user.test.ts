import * as user from '../user';

describe('user handler', () => {
  it('should create a new user', async () => {
    const req = { body: {username: 'hello2', password: 'hi'} };
    const res = {
      json: jest.fn(({token}) => {
        expect(token).toBeTruthy();
      })
    };

    await user.createNewUser(req, res, () => {});
    expect(res.json).toHaveBeenCalledWith(expect.objectContaining({ token: expect.any(String) }));
  })
})